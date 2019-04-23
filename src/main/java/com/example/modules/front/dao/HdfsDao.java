package com.example.modules.front.dao;

import com.example.modules.front.conn.HdfsConn;
import com.example.modules.front.entity.FileEntity;
import com.example.modules.sys.entity.SysUserEntity;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * User: lanxinghua
 * Date: 2019/3/20 11:05
 * Desc: hdfs文件操作
 */
@Repository("hdfsDao")
public class HdfsDao {
    private final String basePath = "/disk/";
    private static final Logger logger = LoggerFactory.getLogger(HdfsDao.class);

    /**
     * 获得在hdfs中的目录 /disk/username/1111/1111.pdf
     *
     * @param file
     * @param user
     * @return
     */
    public String formatPathMethod(SysUserEntity user, FileEntity file) {
        return basePath + user.getUsername() + file.getPath();
    }

    public String formatPathMethodByUserName(String userName, String filePath) {
        return basePath + userName + filePath;
    }


    /**
     * 上传文件
     *
     * @param inputStream
     * @param file
     * @param user
     */
    public void put(InputStream inputStream, FileEntity file, SysUserEntity user) {
        try {
            String formatPath = formatPathMethod(user, file);
            OutputStream outputStream = HdfsConn.getFileSystem().create(new Path(formatPath), new Progressable() {
                @Override
                public void progress() {
                    logger.info("上传文件成功，path:{}", formatPath);
                }
            });
            IOUtils.copyBytes(inputStream, outputStream, 2048, true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建文件夹
     *
     * @param file
     * @param user
     */
    public void mkDir(FileEntity file, SysUserEntity user) {
        try {
            String formatPath = formatPathMethod(user, file);
            if (!HdfsConn.getFileSystem().exists(new Path(formatPath))) {
                HdfsConn.getFileSystem().mkdirs(new Path(formatPath));
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除文件或目录
     *
     * @param file
     * @param user
     */
    public void delete(FileEntity file, SysUserEntity user) {
        try {
            String formatPath = formatPathMethod(user, file);
            if (HdfsConn.getFileSystem().exists(new Path(formatPath))) {
                HdfsConn.getFileSystem().delete(new Path(formatPath), true);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 重命名文件，未使用
     *
     * @param file
     * @param user
     * @param newname
     */
    public void rename(FileEntity file, SysUserEntity user, String newname) {
        try {
            String formatPath = formatPathMethod(user, file);
            file.setName(newname);
            String newformatPath = formatPathMethod(user, file);
            if (HdfsConn.getFileSystem().exists(new Path(formatPath))) {
                HdfsConn.getFileSystem().rename(new Path(formatPath), new Path(newformatPath));
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 下载文件
     *
     * @param userName
     * @param filePath
     * @param local
     */
    public boolean downloadByUserNameAndFilePath(String userName, String filePath, String local) {
        try {
            String formatPath = formatPathMethodByUserName(userName, filePath);
            if (HdfsConn.getFileSystem().exists(new Path(formatPath))) {
                FSDataInputStream inputStream = HdfsConn.getFileSystem().open(new Path(formatPath));
                OutputStream outputStream = new FileOutputStream(local);
                IOUtils.copyBytes(inputStream, outputStream, 4096, true);
                System.out.println(local);
                return true;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 下载文件
     *
     * @param user
     * @param file
     * @param local
     */
    public boolean download(SysUserEntity user, FileEntity file, String local) {
        try {
            String formatPath = formatPathMethod(user, file);
            if (HdfsConn.getFileSystem().exists(new Path(formatPath))) {
                FSDataInputStream inputStream = HdfsConn.getFileSystem().open(new Path(formatPath));
                OutputStream outputStream = new FileOutputStream(local);
                IOUtils.copyBytes(inputStream, outputStream, 4096, true);
                System.out.println(local);
                return true;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 复制或者移动文件或者目录
     *
     * @param user
     * @param sourceFile
     * @param destFile
     * @param flag
     */
    public void copyOrMove(SysUserEntity user, FileEntity sourceFile, FileEntity destFile, boolean flag) {
        try {
            String sourceFormatPath = formatPathMethod(user, sourceFile);
            String destFormatPath = formatPathMethod(user, destFile);
            FileUtil.copy(HdfsConn.getFileSystem(), new Path(sourceFormatPath), HdfsConn.getFileSystem(), new Path(destFormatPath), flag, true, HdfsConn.getConfiguration());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
