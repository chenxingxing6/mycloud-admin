package com.example.modules.sys.vo;

import com.example.modules.sys.entity.SysUserEntity;

import java.io.Serializable;
import java.util.List;

/**
 * User: lanxinghua
 * Date: 2019/4/6 14:04
 * Desc: 系统首页
 */
public class IndexVo implements Serializable {
     //用户信息
    private SysUserEntity user;

    //系统运行时长  0天0小时24分钟
    private String jvmRunTime;

    //内存 9.03G/16G GB
    private String memory;

    //内存百分比  0.5
    private String memoryRate;

    //JVM
    private String jvm;

    //JVM百分比
    private String jvmRate;

    //CPU
    private String cpu;

    //CPU百分比
    private String cpuRate;

    //磁盘
    private String disk;

    //磁盘百分比
    private String diskRate;



    //浏览量 Redis里面取
    private int seeNum;

    //用户数量
    private int userNum;

    //上传文件数量（今天）
    private int todayDiskNum;

    //上传文件数量（all）
    private int totalDiskNum;

    //活跃用户
    private String userName;

    //文件下载列表
    private List<FileDownVo> fileDownVos;

    //系统慢接口
    private List<SlowInterfaceVo> slowInterfaceVos;

    public SysUserEntity getUser() {
        return user;
    }

    public void setUser(SysUserEntity user) {
        this.user = user;
    }

    public String getJvmRunTime() {
        return jvmRunTime;
    }

    public void setJvmRunTime(String jvmRunTime) {
        this.jvmRunTime = jvmRunTime;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getMemoryRate() {
        return memoryRate;
    }

    public void setMemoryRate(String memoryRate) {
        this.memoryRate = memoryRate;
    }

    public String getJvm() {
        return jvm;
    }

    public void setJvm(String jvm) {
        this.jvm = jvm;
    }

    public String getJvmRate() {
        return jvmRate;
    }

    public void setJvmRate(String jvmRate) {
        this.jvmRate = jvmRate;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getCpuRate() {
        return cpuRate;
    }

    public void setCpuRate(String cpuRate) {
        this.cpuRate = cpuRate;
    }

    public String getDisk() {
        return disk;
    }

    public void setDisk(String disk) {
        this.disk = disk;
    }

    public String getDiskRate() {
        return diskRate;
    }

    public void setDiskRate(String diskRate) {
        this.diskRate = diskRate;
    }

    public int getSeeNum() {
        return seeNum;
    }

    public void setSeeNum(int seeNum) {
        this.seeNum = seeNum;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public int getTodayDiskNum() {
        return todayDiskNum;
    }

    public void setTodayDiskNum(int todayDiskNum) {
        this.todayDiskNum = todayDiskNum;
    }

    public int getTotalDiskNum() {
        return totalDiskNum;
    }

    public void setTotalDiskNum(int totalDiskNum) {
        this.totalDiskNum = totalDiskNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<FileDownVo> getFileDownVos() {
        return fileDownVos;
    }

    public void setFileDownVos(List<FileDownVo> fileDownVos) {
        this.fileDownVos = fileDownVos;
    }

    public List<SlowInterfaceVo> getSlowInterfaceVos() {
        return slowInterfaceVos;
    }

    public void setSlowInterfaceVos(List<SlowInterfaceVo> slowInterfaceVos) {
        this.slowInterfaceVos = slowInterfaceVos;
    }
}
