$(document).ready(function() {
	inputInvalided=function(sourceId,sourceType){
		dwr.engine._execute("http://qiqunar.com/ridding", 'BackendBean', 'updateInvalidedSource', sourceId,sourceType,function(flag){callback(flag,sourceId);});
	};
	/**
	 * 插入提交
	 */
	inputSubmit=function(sourceId,sourceType){
		_input= $('#input_'+sourceId).val();
		dwr.engine._execute("http://qiqunar.com/ridding", 'BackendBean', 'updateNewMapInput', sourceId,_input,sourceType, function(flag){callback(flag,sourceId);});
	};
	function callback(flag,sourceId){
		if(!flag){
			alert('操作失败!');
		}else{
			$('#'+sourceId).remove();
		}
	};
});