$(function(){
		   
	//从底部上升的遮罩效果开始
	$(".con").hover(function(){
		$(this).find(".txt1").stop().animate({height:"198px"},200);
		$(this).find(".txt1 h3").stop().animate({paddingTop:"60px"},200);
        $(this).find(".txt1 p").css("display","block");
	},function(){
		$(this).find(".txt1").stop().animate({height:"45px"},200);
		$(this).find(".txt1 h3").stop().animate({paddingTop:"0px"},200);
        $(this).find(".txt1 p").css("display","none");
	});
	//从底部上升的遮罩效果结束
});

function getRecommend(){
	var employeeId=localStorage.getItem('userId');
    $.ajax({
        type: 'POST',
        url:"/EmployeeTask/recommend",
        async: false,                         //将ajax改为同步模式
        data: {
            employeeId:employeeId
        },
        success:function(result) {
            var nameStores = document.getElementsByClassName('recommendH');
            var markStores = document.getElementsByClassName('recommendP');
            var picStores = document.getElementsByClassName('recommendPic');
            for (var i = 0; i < result.length; i++) {
                var taskName, picCode, marks;
                $.ajax({
                    type: 'POST',
                    url: "/EmployeeTask/checkOneTask",
                    async: false,                         //将ajax改为同步模式
                    data: {
                        taskId: result[i]
                    },
                    success: function (result) {
                        taskName = result.taskName;
                        marks = result.marks;
                        taskId = result.taskId;
                        if (result.overallPictureId.length > 0) {
                            picCode = showPicture(taskId,result.overallPictureId[0])
                        } else if (result.rectanglePictureId.length > 0) {
                            picCode = showPicture(taskId,result.rectanglePictureId[0]);
                        } else {
                            picCode = showPicture(taskId,result.boundaryPictureId[0]);
                        }
                        nameStores[i].innerHTML=taskName;
                        markStores[i].innerHTML=taskId+":          "+marks;
                        picStores[i].src=picCode;
                    },
                    error: function (result) {
                        alert("error");
                    }
                });
            }
        },
		error: function (result) {
			alert("error");
		}
    });
}

function getRecommendSimilar(){
    var employeeId=localStorage.getItem('userId');
    var taskId2=localStorage.getItem('taskId');
    $.ajax({
        type: 'POST',
        url:"/EmployeeTask/recommendSimilar",
        async: false,                         //将ajax改为同步模式
        data: {
            employeeId:employeeId,
			taskId:taskId2
        },
        success:function(result) {
            var nameStores = document.getElementsByClassName('recommendH');
            var markStores = document.getElementsByClassName('recommendP');
            var picStores = document.getElementsByClassName('recommendPic');
            for (var i = 0; i < result.length; i++) {
                var taskName, picCode, marks;
                $.ajax({
                    type: 'POST',
                    url: "/EmployeeTask/checkOneTask",
                    async: false,                         //将ajax改为同步模式
                    data: {
                        taskId: result[i]
                    },
                    success: function (result) {
                        taskName = result.taskName;
                        marks = result.marks;
                        taskId = result.taskId;
                        if (result.overallPictureId.length > 0) {
                            picCode = showPicture(taskId,result.overallPictureId[0])
                        } else if (result.rectanglePictureId.length > 0) {
                            picCode = showPicture(taskId,result.rectanglePictureId[0]);
                        } else {
                            picCode = showPicture(taskId,result.boundaryPictureId[0]);
                        }
                        nameStores[i].innerHTML=taskName;
                        markStores[i].innerHTML=taskId+":          "+marks;
                        picStores[i].src=picCode;
                    },
                    error: function (result) {
                        alert("error");
                    }
                });
            }
        },
        error: function (result) {
            alert("error");
        }
    });
}



function showPicture(taskId,pictureId) {
    var picCode="";
    $.ajax({
        type: 'POST',
        url:"/EmployeeTask/getPictureBase64Code",
        async: false,                         //将ajax改为同步模式
        data: {
            taskId:taskId,
            pictureId:pictureId
        },
        success:function(result){
            picCode = result;
        },
        error:function(result){
            alert("error");
        }
    });
    return picCode;
}

function getRecommendIds(){
    var employeeId=localStorage.getItem('userId');
    $.ajax({
        type: 'POST',
        url: "/EmployeeTask/recommend",
        async: false,                         //将ajax改为同步模式
        data: {
            employeeId: employeeId
        },
        success: function (result) {
            return result;
        },
        error: function (result) {
            alert("error");
        }
    });
}

function getRecommendSimilarIds(){
    var employeeId=localStorage.getItem('userId');
    var taskId2=localStorage.getItem('taskId');
    $.ajax({
        type: 'POST',
        url:"/EmployeeTask/recommendSimilar",
        async: false,                         //将ajax改为同步模式
        data: {
            employeeId:employeeId,
            taskId:taskId2
        },
        success: function (result) {
            return result;
        },
        error: function (result) {
            alert("error");
        }
    });
}
