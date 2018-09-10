package stz.backend.service;

import stz.backend.entity.EmployeeInfo;
import stz.backend.entity.EmployerInfo;
import stz.backend.enums.ProfessionType;

public interface RegisterLoginService {

    /**
     * 众包发起者注册
     * @param info: 注册时只需要填写name、id、password、email，其他为系统默认值
     * @return
     */
    public boolean EmployerRegister(EmployerInfo info);

    /**
     * 众包工人注册
     * @param info: 注册时只需要填写name、id、password、email，其他为系统默认值
     * @return
     */
    public boolean EmployeeRegister(EmployeeInfo info);

    /**
     * 用用户编号加密码登录，即id和password匹配即可
     * @param profession:用户身份
     * @param id:用户编号
     * @param password:密码
     * @return
     */
    public boolean login(ProfessionType profession, String id, String password);

    /**
     * 检测用户输入的id是否唯一
     * @param userId
     * @return
     */
    public boolean checkUserId(String userId);

}
