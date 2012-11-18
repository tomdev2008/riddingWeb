<section class="base-topbar">
    <article class="topbar-mod">
        <hgroup class="topbar-logo">
            <h1 class="logo"><a href="/" title="骑去哪儿网" style="color: #CCCCCC;font-size: 22px;text-align: center;font-family:BallparkWeiner;">骑去哪儿网</a></h1>
            <!--<img src="" alt="创建活动步骤"/>-->
        </hgroup>
        <nav class="topbar-nav" style="">
            
            <#if visitUserId?exists&&visitUserId gt 0>
            <ul style="padding:0 3px;height: 36px;">
               <li style="height: 30px;padding:3px 0;float: left;margin:0 10px;"><a href="/user/${visitUserId!0}/ridding/list/" title="${visitNickname!""}" style="block:inline-block;width:30px;height:30px;"><img src="${visitSAvatorUrl!""}" style="border:none;width:30px;height:30px" alt="${visitNickname!""}"/></a></li>
               <li style="height: 36px;float: left;margin:0 10px; line-height: 36px;text-align:center;"><a style="color: #CCCCCC;text-align: center;" href="/user/${visitUserId!0}/ridding/list/" title="${visitNickname!""}"><span>${visitNickname!""}</span><!--<span class="arrow"></span>--></a></li>
               <li style="height: 36px;float: left;margin:0 10px; line-height: 36px;text-align:center;"><a id="quit" style="color: #CCCCCC;text-align: center;" href="javascript:void(0);;">退出</a></li>
            </ul>
            <#else>
            <ul style="padding:0 3px;height: 30px;">
               <li style="float: right;margin:0 10px; line-height: 36px;"><a style="color: #CCCCCC;font-size:20px;" href="/bind/sinabind/">新浪微博登陆</a></li>
            </ul>
            </#if>
        </nav>
    </article>
</section>