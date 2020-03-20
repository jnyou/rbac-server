
import com.blithe.cms.pojo.system.SysUser;
import com.blithe.cms.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (SysUser)表控制层
 *
 * @author makejava
 * @since 2020-03-19 09:55:08
 */
@RestController
@RequestMapping("sysUser")
public class UserController {
    /**
     * 服务对象
     */
    @Autowired
    private SysUserService sysUserService;


}