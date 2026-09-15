package com.huazie.ffs.common.util;

import com.huazie.ffs.common.FleaFSConstants;
import com.huazie.ffs.common.exceptions.FleaFSException;
import com.huazie.fleaframework.common.EncryptionAlgorithmEnum;
import com.huazie.fleaframework.common.exceptions.FleaException;
import com.huazie.fleaframework.common.util.ExceptionUtils;
import com.huazie.fleaframework.common.util.ObjectUtils;
import com.huazie.fleaframework.common.util.RandomCode;
import com.huazie.fleaframework.common.util.SecurityUtils;
import com.huazie.fleaframework.common.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.Key;
import java.util.Base64;

/**
 * FleaFS 文件加解密工具类，用于支持文件类目配置的加密方式【AES、DES】
 *
 * <p> 加密算法标识复用框架枚举 {@link EncryptionAlgorithmEnum}，随机密钥生成复用框架工具类
 * {@link SecurityUtils}；此处仅补充文件维度的流式加解密能力（框架的加解密方法是面向字符串的）。
 * <p> 密钥在上传（或更新）时随机生成，经 Base64 编码后存放于文件信息的 secret_key 列；
 * 每次上传或更新均生成新密钥，版本快照各自持有对应的密钥，保证历史版本可解密。
 * <p> 加解密均采用流式处理，避免大文件占用过多内存。
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class EncryptionUtils {

    /**
     * Cipher 转换串后缀【ECB 模式 + PKCS5Padding 填充，与框架 SecurityUtils 的默认转换方式一致】
     */
    private static final String CIPHER_TRANSFORMATION_SUFFIX = "/ECB/PKCS5Padding";

    private EncryptionUtils() {
    }

    /**
     * 判断指定加密方式是否需要执行文件加密
     *
     * @param encryptType 加密方式【AES、DES、NONE】
     * @return true：需要加密；false：无需加密
     * @since 1.0.0
     */
    public static boolean isEncryptionNeeded(String encryptType) {
        return ObjectUtils.isNotEmpty(getAlgorithm(encryptType));
    }

    /**
     * 按指定加密方式生成随机密钥，并经 Base64 编码返回
     * <p> 密钥生成复用框架工具类 {@link SecurityUtils#createSecretAESKey()}、
     * {@link SecurityUtils#createSecretDESKey()}。
     *
     * @param encryptType 加密方式【AES、DES】
     * @return Base64 编码后的密钥字符串
     * @throws FleaException 加密方式不支持或密钥生成失败时抛出
     * @since 1.0.0
     */
    public static String generateSecretKey(String encryptType) throws FleaException {
        EncryptionAlgorithmEnum algorithm = getAlgorithm(encryptType);
        if (ObjectUtils.isEmpty(algorithm)) {
            // ERROR-ENCRYPTION0000000000 不支持的文件加密方式【{0}】！
            ExceptionUtils.throwFleaException(FleaFSException.class, "不支持的文件加密方式【" + encryptType + "】！");
            return null;
        }

        SecretKey secretKey = EncryptionAlgorithmEnum.AES == algorithm
                ? SecurityUtils.createSecretAESKey()
                : SecurityUtils.createSecretDESKey();
        if (ObjectUtils.isEmpty(secretKey)) {
            ExceptionUtils.throwFleaException(FleaFSException.class, "生成文件加密密钥失败");
            return null;
        }

        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * 加密指定文件，返回加密后的临时文件
     * <p> 加密文件存放于系统临时目录下，上传至 FastDFS 后由调用方负责删除。
     *
     * @param sourceFile  待加密的源文件
     * @param encryptType 加密方式【AES、DES】
     * @param secretKey   Base64 编码的密钥字符串
     * @return 加密后的临时文件
     * @throws FleaException 加密方式不支持、密钥非法或加解密失败时抛出
     * @since 1.0.0
     */
    public static File encryptFile(File sourceFile, String encryptType, String secretKey) throws FleaException {
        ObjectUtils.checkEmpty(sourceFile, FleaFSException.class, "sourceFile must not be null");
        ObjectUtils.checkEmpty(secretKey, FleaFSException.class, "secretKey must not be null");

        File encryptFile = new File(System.getProperty(FleaFSConstants.SystemConstants.JAVA_IO_TMPDIR), RandomCode.toUUID());
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, encryptType, secretKey);
        try (InputStream in = new FileInputStream(sourceFile);
             CipherOutputStream out = new CipherOutputStream(new FileOutputStream(encryptFile), cipher)) {
            byte[] buffer = new byte[FleaFSConstants.IOConstants.BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            ExceptionUtils.throwFleaException(FleaFSException.class, "文件加密失败", e);
        }
        return encryptFile;
    }

    /**
     * 解密文件输入流，返回解密后的输入流（流式处理，调用方负责关闭流）
     *
     * @param inputStream 加密的文件输入流
     * @param encryptType 加密方式【AES、DES】
     * @param secretKey   Base64 编码的密钥字符串
     * @return 解密后的文件输入流
     * @throws FleaException 加密方式不支持、密钥非法或解密失败时抛出
     * @since 1.0.0
     */
    public static InputStream decryptFile(InputStream inputStream, String encryptType, String secretKey) throws FleaException {
        ObjectUtils.checkEmpty(inputStream, FleaFSException.class, "inputStream must not be null");
        ObjectUtils.checkEmpty(secretKey, FleaFSException.class, "secretKey must not be null");

        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, encryptType, secretKey);
        return new CipherInputStream(inputStream, cipher);
    }

    /**
     * 根据加密方式解析出对应的加密算法枚举
     * <p> 仅支持文件加解密场景下的【AES、DES】，其余（含 MD5、SHA-1 等摘要算法）均返回 null。
     *
     * @param encryptType 加密方式
     * @return 加密算法枚举，不支持时返回 null
     * @since 1.0.0
     */
    private static EncryptionAlgorithmEnum getAlgorithm(String encryptType) {
        if (StringUtils.isBlank(encryptType)) {
            return null;
        }
        if (EncryptionAlgorithmEnum.AES.getAlgorithm().equalsIgnoreCase(encryptType)) {
            return EncryptionAlgorithmEnum.AES;
        }
        if (EncryptionAlgorithmEnum.DES.getAlgorithm().equalsIgnoreCase(encryptType)) {
            return EncryptionAlgorithmEnum.DES;
        }
        return null;
    }

    /**
     * 根据加密方式与密钥获取Cipher实例
     *
     * @param cipherMode  Cipher工作模式【加密：Cipher.ENCRYPT_MODE 解密：Cipher.DECRYPT_MODE】
     * @param encryptType 加密方式【AES、DES】
     * @param secretKey   Base64 编码的密钥字符串
     * @return Cipher实例
     * @throws FleaException 加密方式不支持或密钥非法时抛出
     * @since 1.0.0
     */
    private static Cipher getCipher(int cipherMode, String encryptType, String secretKey) throws FleaException {
        EncryptionAlgorithmEnum algorithm = getAlgorithm(encryptType);
        if (ObjectUtils.isEmpty(algorithm)) {
            // ERROR-ENCRYPTION0000000000 不支持的文件加密方式【{0}】！
            ExceptionUtils.throwFleaException(FleaFSException.class, "不支持的文件加密方式【" + encryptType + "】！");
            return null;
        }

        try {
            String transformation = StringUtils.strCat(algorithm.getAlgorithm(), CIPHER_TRANSFORMATION_SUFFIX);
            Key key = new SecretKeySpec(Base64.getDecoder().decode(secretKey), algorithm.getAlgorithm());
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(cipherMode, key);
            return cipher;
        } catch (Exception e) {
            ExceptionUtils.throwFleaException(FleaFSException.class, "获取加解密Cipher实例失败", e);
            return null;
        }
    }
}
