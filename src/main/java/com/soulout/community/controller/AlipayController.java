package com.soulout.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayUserCertifyOpenInitializeRequest;
import com.alipay.api.response.AlipayUserCertifyOpenInitializeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Controller
public class AlipayController {

    @Value("${alipay.client.url}")
    private String CLIENT_URL;

    @Value("${alipay.app.id}")
    private String APP_ID;

    @Value("${alipay.private.key}")
    private String APP_PRIVATE_KEY;

    @Value("${alipay.format}")
    private String FORMAT;

    @Value("${alipay.charset}")
    private String CHARSET;

    @Value("${alipay.public.key}")
    private String ALIPAY_PUBLIC_KEY;

    @Value("${alipay.signType}")
    private String SIGNTYPE;


    @GetMapping("/alipay/certification")
    public void certification() throws AlipayApiException {

        AlipayClient alipayClient = new DefaultAlipayClient(CLIENT_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGNTYPE);
        AlipayUserCertifyOpenInitializeRequest request = new AlipayUserCertifyOpenInitializeRequest();

        //构造身份信息json对象
        JSONObject identityObj = new JSONObject();
        //身份类型，必填，详细取值范围请参考接口文档说明
        identityObj.put("identity_type", "CERT_INFO");
        //证件类型，必填，详细取值范围请参考接口文档说明
        identityObj.put("cert_type", "IDENTITY_CARD");
        //真实姓名，必填
        identityObj.put("cert_name", "黄文宙");
        //证件号码，必填
        identityObj.put("cert_no", "440681199408014216");

        //构造商户配置json对象
        JSONObject merchantConfigObj = new JSONObject();
        // 设置回调地址,必填. 如果需要直接在支付宝APP里面打开回调地址使用alipay协议，参考下面的案例：appId用固定值 20000067，url替换为urlEncode后的业务回跳地址
        // alipays://platformapi/startapp?appId=20000067&url=https%3A%2F%2Fapp.cqkqinfo.com%2Fcertify%2FzmxyBackNew.do
        merchantConfigObj.put("return_url", "https://m.yiihuu.com/zhifubaocertification/result.php");

        //构造身份认证初始化服务业务参数数据
        JSONObject bizContentObj = new JSONObject();
        //商户请求的唯一标识，推荐为uuid，必填
        UUID uuid = UUID.randomUUID();
        bizContentObj.put("outer_order_no", uuid);
        bizContentObj.put("biz_code", "FACE");
        bizContentObj.put("identity_param", identityObj);
        bizContentObj.put("merchant_config", merchantConfigObj);
        request.setBizContent(bizContentObj.toString());

        //发起请求
        AlipayUserCertifyOpenInitializeResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
            //接口调用成功，从返回对象中获取certify_id
            String certifyId = response.getCertifyId();
            //执行后续流程...
        } else {
            System.out.println("调用失败");
        }
    }
}
