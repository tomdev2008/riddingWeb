<!DOCTYPE HTML>
<#escape x as x?html>
<#assign pageName = "backendHuodong" />
<#include "head.ftl">
<style type="text/css">
html,body,#gmap{height:100%; margin:0;}
body{font-size:83%;}
#help{padding-top:20%; text-align:center;}
img{max-width: 100%;max-height: 80%}
div.outset {border-style: none;width: 20%;height: 300px;float:left;clean:both}
</style>
	
<body>
<h3 style="font-size: 20px;color: red;"><a href="/backend/index/">返回</a></h3>

<div>
    <h3>活动名称:<span style="color: red;">${ridding.name!""}</span></h3>
    <h3>活动人数:<span style="color: red;">${ridding.userCount!0}</span></h3>
    <h3>活动创建时间:<span style="color: red;">${ridding.createTimeStr!""}</span></h3>
    <div >喜欢数:<span style="color: red;">${ridding.likeCount!0}</span></div>
    <div >评论数:<span style="color: red;">${ridding.commentCount!0}</span></div>
    <div >使用数:<span style="color: red;">${ridding.useCount!0}</span></div>
    <div >关注数:<span style="color: red;">${ridding.careCount!0}</span></div> 
    <div >推荐:<span style="color: red;"><#if ridding.isRecom == 1>是<#else>否</#if></span></div> 
    <div >公开:<span style="color: red;"><#if ridding.isPublic == 1>是<#else>否</#if></span></div> 
    <div >同步到新浪微博:<span style="color: red;"><#if ridding.isSyncSina == 1>是<#else>否</#if></span></div>
    
    <#if riddingPictures?exists>
    <div>
       <#list riddingPictures as picture>
       		<div class="outset" style="text-align: center;">
            <img src="${cfg_imageHost}${picture.photoUrl!""}?imageView/1/w/100" id="huodong_img_${picture.id!0}"/><br>
           	<span>${picture.description!""}</span><br>
           	</div>
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