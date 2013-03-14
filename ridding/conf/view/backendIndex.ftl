<!DOCTYPE HTML>
<#escape x as x?html>
<#assign pageName = "backendIndex" />
<style>
html,body,#gmap{height:100%; margin:0;}
body{font-size:83%;}
#help{padding-top:20%; text-align:center;}
</style>
<body>
<h3><a href="/backend/user/#{visitUserId}/sendWeiBo/">发微博</a></h3>
<h3><a href="/backend/user/#{visitUserId}/huodong/recom/"">设置推荐活动</a></h3>
<h3><a href="/backend/user/#{visitUserId}/huodong/list/">查看活动列表</a></h3>
<h3><a href="/backend/ridding/0/comment/">查看评论列表</a></h3>
<h3><a href="/backend/ridding/vip/">查看升级列表</a></h3>
<h3><a href="/backend/user/#{visitUserId}/feedback">查看反馈列表</a></h3>
</section>

</body>
<script>

</script>
</html>
</#escape>
<#include "js.ftl">