package cn.samples.depot.web.oauth.jwt;

import cn.samples.depot.common.model.CRUDView;
import cn.samples.depot.web.entity.CRoles;
import cn.samples.depot.web.entity.CUsers;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Description:
 *
 * @className: UserContext
 * @Author: ChenJie
 * @Date 2019/7/16 13:58
 * @Version 1.0
 **/
public class UserContext {
    @JsonView({CRUDView.class})
    private final CUsers user;
    @JsonView({CRUDView.class})
    private final CRoles role;

    private UserContext(CUsers user, CRoles role) {
        this.user = user;
        this.role = role;
    }

    public static UserContext create(CUsers user, CRoles role) {
        if (user == null || role == null) throw new IllegalArgumentException("user or role is null");
        return new UserContext(user, role);
    }

    public CUsers getUser() {
        return user;
    }

    public CRoles getRole() {
        return role;
    }
}
