
import com.jingmi.chat.common.MallchatCustomApplication;
import com.jingmi.chat.common.common.utils.JwtUtils;
import com.jingmi.chat.common.user.dao.UserDao;
import com.jingmi.chat.common.user.domain.entity.User;
import com.jingmi.chat.common.user.domain.enums.IdempotentEnum;
import com.jingmi.chat.common.user.domain.enums.ItemEnum;
import com.jingmi.chat.common.user.service.IUserBackpackService;
import com.jingmi.chat.common.user.service.LoginService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @program: jingmiChat
 * @description: 测试用例
 * @author: JingMi
 * @create: 2024-07-13 14:24
 **/
@SpringBootTest(classes = MallchatCustomApplication.class)
@RunWith(SpringRunner.class)
public class DaoTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private LoginService loginService;
    @Autowired
    private IUserBackpackService iUserBackpackService;
    public static final Long UID =20001L;

    @Test
    public void acquireItem(){
        iUserBackpackService.acquireItem(UID, ItemEnum.PLANET.getId(), IdempotentEnum.UID,UID+"");
    }
    @Test
    public void jwt (){
        String token = jwtUtils.createToken(1L);
        System.out.println("token==>"+token);

    }

    @Autowired
    private RedisTemplate redisTemplate ;
    @Test
    public void redis() {
        redisTemplate.opsForValue().set("name","卷心菜");
        String name = (String) redisTemplate.opsForValue().get("name");
        System.out.println(name); //卷心菜
    }
    @Test
    public void vaildToken() {

        Long validUid = loginService.getValidUid("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjIwMDAxLCJjcmVhdGVUaW1lIjoxNzIxMTQzOTQyfQ.BfRkumZ_K4W1hQoB_EB9LG8LoTU8Ynchrw5b-swjfHs");
        System.out.println("validUid==>"+validUid);
    }
    @Test
    public void redisSsion() {
        RLock abc = redissonClient.getLock("abc");
        abc.lock();
        System.out.println("wc");
        abc.unlock();
    }


    // 测试用例
    @Test
    public void test() throws WxErrorException {
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(1, 1000);
        wxMpQrCodeTicket.getUrl();
        System.out.println(" wxMpQrCodeTicket.getUrl();==>" +wxMpQrCodeTicket.getUrl());

    }
}