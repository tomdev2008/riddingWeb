<#-- ϵͳ���õ���freemarker�����Ķ��� -->

<#function isLogIn><#return !anonymous></#function>
<#function completeURL url><#if url?has_content && url!="" && !url?contains("http://")><#return "http://"+url><#else><#return url></#if></#function>
<#function needAutoLogin><#return autoLoginFlag?? && autoLoginFlag==true></#function>
<#-- ���� substring(str,num) num�Ǽ�����ʾ����-->
<#function substring str num>
	<#if str?length lte num>
		<#return str>
	<#else>
		<#return str[0..(num-1)]+"...">
	</#if>
</#function>

<#-- ��ȡ�ַ��� -->
<#function truncate str limit=20>
	<#if !str?has_content>
		<#return ''/>
	</#if>
	<#local len=0/>
	<#list 0..str?length-1 as i>
		<#if str[i]?matches("[^u4e00-u9fa5]")>
			<#local len=len+2/>
		<#else>
			<#local len=len+1/>
		</#if>
		<#if len&gt;limit>
			<#return str?substring(0,i)+'��'/>
		</#if>
	</#list>
	<#return str/>
</#function>

<#-- �û�С�ݵ�ַ homeurl(username)-->
<#function homeurl username>
	<#return "/"+username+"/home/">
</#function>

<#-- �����滻freemark Ĭ�ϣ�����-->
<#function getValue arg defultValue="">
	<#if arg??>
		<#if arg?is_string && arg?length&gt;0>
			<#return arg>
		<#else>
			<#return defultValue>
		</#if>
		<#-- ����Ҫ���Ʊ�����͵��ټ� -->
		<#return arg>
	<#else>
		<#return defultValue>
	</#if>
</#function>

<#-- ȡ�����ͷ��Ĭ��Ŀǰ�ṩ����ͷ��
/**
 * �õ��û�ͷ�� ��Դ��� ����
 * �ִ���trim֮��������ַ���http����Ϊ������·��ֱ�ӷ���
 * @param {String} _userName
 * @param {Number} _type 0 - 60*60px��1 - 140*140px
 * @param {Number} _tag  1 - photo; 2 - blog; Ĭ��blog
 * 	��Ϊ˽�У��������ⲿעֵ,Ŀǰ����ȡ���͵�
 */
-->
<#function getPhotoAva userName='' type=0>
	 <#local userName=userName?string?trim userName1 = '${cfg_static_proot_126net}/image/default/duser.png' userName2 = '${cfg_static_proot_126net}/image/default/duser140.png' tag = 1 />
		<#if userName?? && userName?index_of('http://') == 0 >
			<#return userName />
		<#elseif userName?? && userName != ''>
			<#switch tag>
				<#case 1>
					<#if type?? && type == 1>
						<#local userName1 = 'http://ava.ph.126.net/' + userName + '/140x140x0x85.jpg' />
					<#else>
						<#local userName1 = 'http://ava.ph.126.net/' + userName + '/60x60x0x85.jpg' />
					</#if>
					<#break>
				<#case 2>
					<#if type?? && type == 1>
						<#local userName1 = 'http://os.blog.163.com/common/ava.s?passport=' + userName + '&b=1' />
					<#else>
						<#local userName1 = 'http://os.blog.163.com/common/ava.s?passport=' + userName + '&b=0' />
					</#if>
					<#break>
				<#default>
					<#if type?? && type == 1>
						<#local userName1 = 'http://os.blog.163.com/common/ava.s?passport=' + userName + '&b=1' />
					<#else>
						<#local userName1 = 'http://os.blog.163.com/common/ava.s?passport=' + userName + '&b=0' />
					</#if>
					<#break>			
			</#switch>
		<#elseif type?? && type == 1>
			<#local userName1 = userName2 />
		</#if>
		<#return userName1 />
</#function>
<#-- /ȡ�����ͷ��end -->

<#-- �ж��Ƿ��ڼ���֮�� verifyDay(time1,time2,n)-->
<#function verifyDay time1 time2 n>
	 <#local times = time2 - time1>
	 <#if times lte n*24*3600*1000 >
	 	<#return 1>
	 <#else>
	 	<#return 0>
	 </#if>
</#function>


<#-- ����û����Զ�����������  userName �û���  pageSetting �û���Ϣ domainBefore ��ʶ������ǰ�ں� -->
<#function getUserDynamicUrl userName pageSetting domainBefore=false>
	<#if pageSetting?? && pageSetting.domainName?? && pageSetting.domainName?trim != ''>
			<#if domainBefore>
					<#return "http://"+pageSetting.domainName+".pp.163.com">
				<#else>
					<#return cfg_proot + "/"+pageSetting.domainName>
			</#if>
		<#else>
			<#return cfg_root+"/" + userName>
	</#if>
</#function>

<#-- ����û����Զ����������ӵ�С��ҳ -->
<#function getUserHomeUrl userName domain="">
	<#if hostPageSettingdomain?? && domain?trim != ''>
			<#return "http://"+domain+".pp.163.com">
		<#else>
			<#return cfg_root+"/"+userName+"/home">
	</#if>
</#function>

<#-- ���visit�û����Զ����������ӵ�С��ҳ -->
<#function getVisitorHomeUrl>
	<#if visitPageSetting?? && visitPageSetting.domainName?? && visitPageSetting.domainName?trim != ''>
		<#return "http://"+visitPageSetting.domainName+".pp.163.com">
	<#else>
		<#return cfg_root+"/"+vUserName+"/home">
	</#if>
</#function>

<#-- ���host�û����Զ�����������  domainBefore ��ʶ������ǰ�ں�  photo20�����Ƿ����Զ�������  -->
<#function getHostDynamicUrl domainBefore=false photo20=host_photo2_0>
	<#if photo20>
		<#if hostPageSetting??>
			<#return getUserDynamicUrl(hUserName,hostPageSetting,domainBefore)>
			<#else>
			<#return cfg_root+"/"+hUserName>
		</#if>
	<#else>
		<#return cfg_root+"/"+hUserName>
	</#if>
</#function>
<#-- ���host�û�С��ҳ����  -->
<#function getDynamicHostHomeUrl>
	<#if host_photo2_0 && hostPageSetting?? && hostPageSetting.domainName?? && hostPageSetting.domainName?trim != ''>
		<#return "http://"+hostPageSetting.domainName+".pp.163.com">
		<#else>
		<#return cfg_root+"/"+hUserName+"/home">
	</#if>
</#function>

<#-- ���host�û����ҳ����  -->
<#function getDynamicHostAlbumUrl>
	<#if host_photo2_0 && hostPageSetting?? && hostPageSetting.domainName?? && hostPageSetting.domainName?trim != ''>
		<#return "http://"+hostPageSetting.domainName+".pp.163.com/album">
		<#else>
		<#return cfg_root+"/"+hUserName>
	</#if>
</#function>

<#-- ���visit�û����ҳ����  -->
<#function getDynamicVisitAlbumUrl>
	<#if visit_photo2_0 && visitPageSetting?? && visitPageSetting.domainName?? && visitPageSetting.domainName?trim != ''>
		<#return "http://"+visitPageSetting.domainName+".pp.163.com/album">
		<#else>
		<#return cfg_root+"/"+vUserName>
	</#if>
</#function>

<#-- ƴװcss/js·��  
sourceArray ['share|**/**']
sourceTag [css | js]
-->
<#function getStaticSourceUrl sourceArray sourceTag>
	 <#local urlStr = ''/>
	 <#if sourceArray?? && sourceArray?size gt 0>
	 <#list sourceArray as urlItem>
	 	<#local tmpArray = urlItem?trim?split('|') />
	 	<#if tmpArray?? && tmpArray?size gt 1>
			<#local splitStr = '/' />
			<#if tmpArray[1]?index_of('/') == 0>
				<#local splitStr = '' />
			</#if>
	 		<#switch tmpArray[0] >
	 			<#case 'global'>
	 				<#if sourceTag== 'css'>
	 					<#local urlStr = urlStr + '<link type="text/css" rel="stylesheet" href="' + cfg_global_css + splitStr + tmpArray[1] + '?v=' + cfg_version_css + '"/>' />
	 				<#elseif sourceTag == 'js'>
	 					<#local urlStr = urlStr + '<script type="text/javascript" src="' + cfg_global_js + splitStr + tmpArray[1] + '?v=' + cfg_version_js + '"></script>' />
	 				</#if>
	 			<#break/>		
	 			<#case 'photo'>
	 				<#if sourceTag== 'css'>
	 					<#local urlStr = urlStr + '<link type="text/css" rel="stylesheet" href="' + cfg_photo_root_css + splitStr + tmpArray[1] + '?v=' + cfg_version_css + '"/>' />
	 				<#elseif sourceTag == 'js'>
	 					<#local urlStr = urlStr + '<script type="text/javascript" src="' + cfg_photo_root_js + splitStr + tmpArray[1] + '?v=' + cfg_version_js + '"></script>' />
	 				</#if>
	 			<#break/>		
	 			<#case 'share'>
	 				<#if sourceTag== 'css'>
	 					<#local urlStr = urlStr + '<link type="text/css" rel="stylesheet" href="' + cfg_share_root_css + splitStr + tmpArray[1] + '?v=' + cfg_version_css + '"/>' />
	 				<#elseif sourceTag == 'js'>
	 					<#local urlStr = urlStr + '<script type="text/javascript" src="' + cfg_share_root_js + splitStr + tmpArray[1] + '?v=' + cfg_version_js + '"></script>' />
	 				</#if>
	 			<#break/>	
	 			<#case 'huodong'>
	 				<#if sourceTag== 'css'>
	 					<#local urlStr = urlStr + '<link type="text/css" rel="stylesheet" href="' + cfg_hd_root_css + splitStr + tmpArray[1] + '?v=' + cfg_version_css + '"/>' />
	 				<#elseif sourceTag == 'js'>
	 					<#local urlStr = urlStr + '<script type="text/javascript" src="' + cfg_hd_root_js + splitStr + tmpArray[1] + '?v=' + cfg_version_js + '"></script>' />
	 				</#if>
	 			<#break/>	
			</#switch>
	 	</#if>
	 </#list>
	 </#if>
	 <#return urlStr>
</#function>
<#-- /ƴװcss/js·��  -->

<#-- 
����������ͼƬ��ߣ��������顾����width=xx height=xx" ,width,height��
-->
<#function getImgSizeString owidth oheight targetSize adjust = false>
<#if !owidth?? || owidth <= 0>
	<#local owidth = 1 />
</#if>
<#if !oheight?? || oheight <= 0>
	<#local oheight = 1 />
</#if>
<#local urlStr = '' attr = 'width' attr2 = owidth />
<#if owidth lt oheight>
	<#local attr='height' attr2 = oheight />
</#if>
<#if adjust>
	<#if attr == 'width'>
		<#local  oheight =  (oheight * targetSize / owidth)?ceiling owidth = targetSize  />
	<#elseif attr == 'height'>
		<#local  oheight = targetSize  owidth = (owidth * targetSize / oheight)?ceiling />
	</#if>
<#else>
	<#if attr2 lte targetSize>
		
	<#elseif attr == 'width'>
		<#local  oheight =  (oheight * targetSize / owidth)?ceiling owidth = targetSize  />
	<#elseif attr == 'height'>
		<#local  oheight = targetSize  owidth = (owidth * targetSize / oheight)?ceiling />
	</#if>
</#if>
<#local  urlStr = 'width=' + owidth + ' ' + 'height=' + oheight />
<#local  urlStr = [urlStr,owidth,oheight]/>
 <#return urlStr>
</#function>
<#-- ����������ͼƬ���  -->