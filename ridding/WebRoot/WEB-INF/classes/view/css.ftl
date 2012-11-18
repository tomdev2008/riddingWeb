<#-- css文件加载ftl -->
<#if pageName?exists>
     <link type="text/css" rel="stylesheet" href="/css/base.css"/>
     <link type="text/css" rel="stylesheet" href="/css/riddingBase.css"/>
     <link type="text/css" rel="stylesheet" href="/css/topBar.css"/>
	<#switch pageName>
    	<#case "index">
    	
    	<#break>
    	<#case "webIndex">
    	     <link type="text/css" rel="stylesheet" href="/css/webIndex.css"/>
    	<#break>
    	<#case "checkSinaWeiBo">
    	
    	<#break>
    	<#case "mapCreate">
    	    <link type="text/css" rel="stylesheet" href="/css/mapCreate.css"/>
    	<#break>
    	<#case "mapCreate_1">
    	    <link type="text/css" rel="stylesheet" href="/css/mapCreate.css"/>
    	    <link type="text/css" rel="stylesheet" href="/css/mapCreate_1.css"/>
    	<#break>
    	<#case "riddingList">
    	    <link type="text/css" rel="stylesheet" href="/css/riddingList.css"/>
    	<#break>
    	<#case "sendWeiBo">
    	    <link type="text/css" rel="stylesheet" href="/css/backend/jquery-calendar.css"/>
    	<#break>
    </#switch>
</#if>