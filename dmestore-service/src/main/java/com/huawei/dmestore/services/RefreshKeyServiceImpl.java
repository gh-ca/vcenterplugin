package com.huawei.dmestore.services;

import com.huawei.dmestore.dao.DmeInfoDao;
import com.huawei.dmestore.dao.VCenterInfoDao;
import com.huawei.dmestore.entity.DmeInfo;
import com.huawei.dmestore.entity.VCenterInfo;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.utils.AESCipher;
import com.huawei.dmestore.utils.CipherUtils;
import com.huawei.dmestore.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;

import static com.huawei.dmestore.utils.AESCipher.KEY_SIZE;

public class RefreshKeyServiceImpl implements RefreshKeyService{

    private VCenterInfoDao vCenterInfoDao;

    private DmeInfoDao dmeInfoDao;

    private CipherUtils cipherUtils;

    public CipherUtils getCipherUtils() {
        return cipherUtils;
    }

    public void setCipherUtils(CipherUtils cipherUtils) {
        this.cipherUtils = cipherUtils;
    }

    public VCenterInfoDao getvCenterInfoDao() {
        return vCenterInfoDao;
    }

    public void setvCenterInfoDao(VCenterInfoDao vCenterInfoDao) {
        this.vCenterInfoDao = vCenterInfoDao;
    }

    public DmeInfoDao getDmeInfoDao() {
        return dmeInfoDao;
    }

    public void setDmeInfoDao(DmeInfoDao dmeInfoDao) {
        this.dmeInfoDao = dmeInfoDao;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshKeyServiceImpl.class);
    @Override
    public void refreshKey() throws DmeException {
        LOGGER.info("start refresh key");
        try {
            VCenterInfo vcenterInfo = vCenterInfoDao.getVcenterInfo();
            if (vcenterInfo != null) {
                vcenterInfo.setPassword(cipherUtils.decryptString(vcenterInfo.getPassword()));
            }
            DmeInfo dmeInfo = dmeInfoDao.getDmeInfo();
            if (dmeInfo != null) {
                dmeInfo.setPassword(dmeInfo.getPassword());
            }

            //更新密钥，重新加密
            String fileStringKey = AESCipher.getSafeRandomToString(KEY_SIZE);
            FileUtils.saveKey(fileStringKey, FileUtils.BASE_FILE_NAME);

            String workKey = cipherUtils.getAesCipher().genencryptWorkKey();
            FileUtils.saveKey(workKey, FileUtils.WORK_FILE_NAME);

            if (vcenterInfo != null) {
                vcenterInfo.setPassword(cipherUtils.encryptString(vcenterInfo.getPassword()));
                vCenterInfoDao.updateVcenterInfo(vcenterInfo);
            }

            if (dmeInfo != null) {
                dmeInfo.setPassword(cipherUtils.encryptString(dmeInfo.getPassword()));
                dmeInfoDao.updateDmeInfo(dmeInfo);
            }
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Failed to refresh key: " + e.getMessage());
        }
    }
}
