<#-- js文件加载ftl -->
<#if pageName?exists>
    <script type="text/javascript" src="/js/base/engine.js" charset="utf-8"></script>
    <script type="text/javascript" src="/js/base/util.js" charset="utf-8"></script>
    <script type="text/javascript" src="/js/base/jquery-1.7.2.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="/js/base/jquery.form.js" charset="utf-8"></script>
    <script type="text/javascript" src="/js/base/jquery.easyui.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="http://tjs.sjs.sinajs.cn/open/api/js/wb.js?appkey=4233231260" charset="utf-8"></script>
    <script type="text/javascript" src="/js/base/riddingBase.js" charset="utf-8"></script>
    
	<#switch pageName>
    	<#case "index">
    	
    	<#break>
    	<#case "checkSinaWeiBo">
    	<script type="text/javascript" src="/js/CheckUrlView.js"></script>
    	<#break>
    	<#case "mapCreate">
    	<!-- maps.googleapis ditu.google-->
    	<script type="text/javascript" src="http://ditu.google.com/maps/api/js?sensor=false" charset="utf-8"></script>
    	<script type="text/javascript" src="/js/mapCreate_Invite.js" charset="utf-8"></script>
    	<script type="text/javascript" src="/js/mapCreate.js" charset="utf-8"></script>
    	<#break>
    	<#case "mapCreate_1">
    	<script type="text/javascript" src="http://ditu.google.com/maps/api/js?sensor=false" charset="utf-8"></script>
    	<script type="text/javascript" src="/js/mapCreate_1.js" charset="utf-8"></script>
    	<#break>
    	<#case "riddingList">
    	    <script type="text/javascript" src="/js/riddingList.js" charset="utf-8"></script>
    	<#break>
    	<#case "sendWeiBo">
    	    <script type="text/javascript" src="/js/backend/jquery-calendar.js" charset="utf-8"></script>
    	    <script type="text/javascript" src="/js/backend/sendWeiBo.js" charset="utf-8"></script>
    	<#break>
    	<#case "huodongRecom">
    	    <script type="text/javascript" src="/js/backend/jquery-calendar.js" charset="utf-8"></script>
    	    <script type="text/javascript" src="/js/backend/huodongRecom.js" charset="utf-8"></script>
    	<#break>
    	<#case "huodongList">
    	    <script type="text/javascript" src="/js/backend/huodongList.js" charset="utf-8"></script>
    	<#break>
    	<#case "backendVip">
    	    <script type="text/javascript" src="/js/backend/backendVip.js" charset="utf-8"></script>
    	<#break>
    </#switch>
</#if>