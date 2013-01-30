<!DOCTYPE HTML>
<#escape x as x?html>
<#assign pageName = "backendHuodong" />
<#include "head.ftl">
<style>
html,body,#gmap{height:100%; margin:0;}
body{font-size:83%;}
#help{padding-top:20%; text-align:center;}
</style>
<body>
<h3 style="font-size: 20px;color: red;"><a href="/backend/index/">返回</a></h3>

<div>
    <h3>活动名称:${ridding.name!""}</h3>
    <h3>活动人数:${ridding.userCount!0}</h3>
    <h3>活动创建时间:${ridding.createTimeStr!""}</h3>
    <div >喜欢数:${ridding.likeCount!0}</div>
    <div >评论数:${ridding.commentCount!0}</div>
    <div >使用数:${ridding.useCount!0}</div>
    <div >关注数:${ridding.careCount!0}</div> 
    <div >推荐:<#if ridding.isRecom == 1>是<#else>否</#if></div> 
    <div >公开:<#if ridding.isPublic == 1>是<#else>否</#if></div> 
    <div >同步到新浪微博:<#if ridding.isSyncSina == 1>是<#else>否</#if></div>
    
    <#if riddingPictures?exists>
    <div>
       <#list riddingPictures as picture>
             <img style="width:100px;height:100px;" src="${cfg_imageHost}${picture.photoUrl!""}" id="huodong_img_${picture.id!0}"/>
             <span>${picture.description!""}</span>
        </#list>
    </div>
    </#if>
</div>

<#include "footer.ftl">
</body>
<script>

</script>
</html>
</#escape>
<#include "js.ftl">