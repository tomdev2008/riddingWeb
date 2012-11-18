<!DOCTYPE HTML>
<#escape x as x?html>
<#assign pageName = "webIndex" />
<#include "head.ftl">
<style>
html,body,#gmap{height:100%; margin:0;}
body{font-size:83%;}
#help{padding-top:20%; text-align:center;}
</style>
<body style="">
<#include "base-topbar.ftl">
<aside class="" style="height:590px;background:url('http://qiqunar.com.cn/image/index/index_bg.png?v=20121020') no-repeat scroll center top transparent;">
   <!--<p style="position: absolute; background-color: rgb(255, 255, 224); border: 1px solid rgb(247, 203, 145); text-align: center; height: 25px; line-height: 25px; top: 35px; width: 750px; left: 20%;">目前,app正在审核中。iphone越狱用户，可直接通过手机使用浏览器登陆骑去哪儿网，即可下载《骑行者》</p>-->
   <div style="padding-top: 200px; width: 500px;  margin-left: 53%;">
   <div style="width: 500px; height: 190px;">
      <h3 style="margin-bottom: 25px; margin-top: 10px; font-size: 16px; color: rgb(251, 251, 243); font-family: BebasNeueRegular; width: 400px;">你是否因为《转山》也埋下了征服川藏线的种子。也被一句“骑出去了，就要骑回来”所感动！和我们一起去实现这个梦想吧。</h3>
      <div style="float: right; margin-right: 100px;">
         <!--<a style="border: 1px solid rgb(17, 160, 238); display: block; border-radius: 7px 7px 7px 7px;" href="itms-services://?action=download-manifest&url=http://qiqunar.com.cn/app/qiqunar.plist" class="appimg iosimg1 ios" ></a>-->
         <a style="border: 1px solid rgb(17, 160, 238); display: block; border-radius: 7px 7px 7px 7px;" href="https://itunes.apple.com/us/app/qi-xing-zhe/id557123754?ls=1&mt=8" class="appimg iosimg1 ios" ></a>
      </div>
      <div>
         <a href="/bind/sinabind/" style="background: none repeat scroll 0 0 #1C9FE5;border: 1px solid #11A0EE; border-radius: 7px;color: #FFFFFF;display: inline-block;font-size: 14px;height: 40px;line-height: 40px;opacity: 1;text-align: center;width: 200px;"><span>使用新浪微博登录</span></a>
   
      </div>
   </div>
   <!--
   <div style="width: 300px; padding: 10px;margin-top: 10px;background:url('http://qiqunar.com.cn/image/index/iphone.png?v=20120901') no-repeat scroll 0 bottom #FFFFFF;">
   </div>
   -->
   </div>
</aside>


<#include "footer.ftl">
</body>
</html>
</#escape>
<#include "js.ftl">