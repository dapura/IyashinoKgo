package info.jabara.iyashino_kgo.service;

import jabara.general.Sort;

import java.util.List;

import info.jabara.iyashino_kgo.entity.EUser;
import info.jabara.iyashino_kgo.service.impl.UserServiceImpl;

import com.google.inject.ImplementedBy;

/**
 * 
 */
@ImplementedBy(UserServiceImpl.class)
public interface IUserService {

    /**
     * @param pSort ソート条件.
     * @return 全件.
     */
    List<EUser> getAll(Sort pSort);

    /**
     * 
     */
    void insertAdministratorIfNotExists();
}
