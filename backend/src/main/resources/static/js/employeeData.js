function RankInfo(userId,userName,employerData,employerRank){
    this.userId=userId;//用户账号
    this.userName=userName;//用户名
    this.employerData=employerData;//特定数据
    this.employerRank=employerRank;//数据排名
}
function AnnotationTypeInfo(overall,rect,bound){
    this.overall=overall;
    this.rect=rect;
    this.bound=bound;
}

function TaskTypeInfo(openTask,specifyTask,cutPartTask){
    this.openTask=openTask;//自由发布任务数
    this.specifyTask=specifyTask;//指派发布任务数
    this.cutPartTask=cutPartTask;//分块发布任务数
}

function PictureTypeNum(pictureType, typeNum){
    this.pictureType = pictureType;
    this.typeNum = typeNum;
}

function Ability(accuracy,efficiency,num,range,focus){
    this.accuracy=accuracy;
    this.efficiency=efficiency;
    this.num=num;
    this.range=range;
    this.focus=focus;
}

//返回ability：五个数据均为100分制
//accuracy:100*accuracy(employeeInfo)
//efficiency:100*efficiency(employeeInfo)
//num:已完成任务10个及以上满分，小于10个按比例扣分（嫌简单可以和其他用户比较根据完成任务数排名打分）
//range:标注的任务类型8个及以上满分，小于8个按比例扣分
//focus:标注次数最多的一种类型任务的比例占总任务比例40%满分.小于40%按比例扣分
function getAbility(employeeId){
    var num = getOneEmployeeInfo(employeeId);
    var efficiency = 0;
    if(num[1]<600)
        efficiency = 100;
    else
        efficiency = 100 - (num[1] - 600)*0.0035;
    if(efficiency < 0)
        efficiency = 0;

    var taskIds = getCompletedTasks(employeeId);
    var taskNumScore = 0;
    if(taskIds.length>=10)
        taskNumScore = 100;
    else
        taskNumScore = 100 - (10 - taskIds.length)*10;

    var rangeNum = getPictureType(employeeId);
    var rangeScore = 0;
    if(rangeNum.length>=8)
        rangeScore = 100;
    else
        rangeScore = 100 -(8 - rangeNum.length)*12.5;
    var maxType = 0;
    var storeTotalNum = 0;
    for(var i = 0; i < rangeNum.length; i++){
        storeTotalNum += rangeNum[i].typeNum;
        if(maxType<rangeNum[i].typeNum)
            maxType = rangeNum[i].typeNum;
    }
    var focus = 0;
    if(maxType/storeTotalNum>=0.4)
        focus = 100;
    else
        focus = 100 - (0.4 - maxType/storeTotalNum)*250;

    var result = new Ability(num[0]*100,efficiency,taskNumScore,rangeScore,focus);
    return result;
}

function getOneEmployeeInfo(employeeId) {
    var num = [];
    $.ajax({
        type: 'POST',
        url:"/EmployeeInfo/showEmployeeInfo",
        async: false,                         //将ajax改为同步模式
        data: {
            employeeId:employeeId
        },
        success:function(result){
            num[0] = result.judgement.accuracy;
            num[1] = result.judgement.efficiency;
        },
        error:function(result){
            alert("error");
        }
    });
    return num;
}

//返回rankinfo[]，与上1类似
function getCompletedTaskRank(employeeId){
    var rank = [];
    $.ajax({
        type: 'POST',
        url:"/UserManagement/showEmployeeInfo",
        async: false,                         //将ajax改为同步模式
        data: {},
        success:function(result){
            for(var count = 0; count < result.length; count++){
                var temp = new RankInfo(result[count].employeeId, result[count].employeeName, result[count].taskCompleted, count);
                rank.push(temp);
            }
            for(var count = 0; count < rank.length; count++){
                for(var count2 = 0; count2 < rank.length - 1; count2++){
                    if(rank[count2].employerData < rank[count2+1].employerData){
                        var temp = rank[count2];
                        rank[count2] = rank[count2+1];
                        rank[count2+1] = temp;
                    }
                }
            }
        },
        error:function(result){
            alert("error");
        }
    });
    for(var count =0; count < rank.length; count++){
        rank[count].employerRank = count+1;
    }
    if(rank.length<=10){
        return rank;
    }else{
        var res = [];
        for(var count=0; count<10;count++){
            res[count] = rank[count];
        }
        for(var count = 0; count < rank.length; count++){
            if(rank[count].employeeId == employeeId){
                res[10] = rank[count];
            }
        }
        return res;
    }
}

//返回rankinfo[],与上1类似
function getAwardRank(employeeId){
    var rank = [];
    $.ajax({
        type: 'POST',
        url:"/UserManagement/showEmployeeInfo",
        async: false,                         //将ajax改为同步模式
        data: {},
        success:function(result){
            for(var count = 0; count < result.length; count++){
                var temp = new RankInfo(result[count].employeeId, result[count].employeeName, result[count].awardAmount, count);
                rank.push(temp);
            }
            for(var count = 0; count < rank.length; count++){
                for(var count2 = 0; count2 < rank.length - 1; count2++){
                    if(rank[count2].employerData < rank[count2+1].employerData){
                        var temp = rank[count2];
                        rank[count2] = rank[count2+1];
                        rank[count2+1] = temp;
                    }
                }
            }
        },
        error:function(result){
            alert("error");
        }
    });
    for(var count =0; count < rank.length; count++){
        rank[count].employerRank = count+1;
    }
    if(rank.length<=10){
        return rank;
    }else{
        var res = [];
        for(var count=0; count<10;count++){
            res[count] = rank[count];
        }
        for(var count = 0; count < rank.length; count++){
            if(rank[count].employeeId == employeeId){
                res[10] = rank[count];
            }
        }
        return res;
    }
}

//返回AnnotationTypeInfo;对象属性为该工人所有任务中三种标注类型的图片数量
function getTaskAnnotationType(employeeId){
    var annotationType = new AnnotationTypeInfo(0, 0, 0);
    $.ajax({
        type: 'POST',
        url:"/EmployeeTask/showCompletedTasks",
        async: false,                         //将ajax改为同步模式
        data: {
            employeeId:employeeId
        },
        success:function(result){
            for(var count = 0; count < result.length; count++){
                annotationType.overall += getTaskPictureType(result[count].taskId).overall;
                annotationType.rect += getTaskPictureType(result[count].taskId).rect;
                annotationType.bound += getTaskPictureType(result[count].taskId).bound;
            }
        },
        error:function(result){
            alert("error");
        }
    });
    return annotationType;
}
function getTaskPictureType(taskId) {
    var type = new AnnotationTypeInfo(0, 0, 0);
    $.ajax({
        type: 'POST',
        url:"/EmployeeTask/checkOneTask",
        async: false,                         //将ajax改为同步模式
        data: {
            taskId:taskId
        },
        success:function(result){
            type.overall = result.overallPictureId.length;
            type.rect = result.rectanglePictureId.length;
            type.bound = result.boundaryPictureId.length;
        },
        error:function(result){
            alert("error");
        }
    });
    return type;
}

//返回rankinfo[]，根据employeeInfo里的评价积分排名
function getIntegrationRank(employeeId){
    var rank = [];
    $.ajax({
        type: 'POST',
        url:"/UserManagement/showEmployeeInfo",
        async: false,                         //将ajax改为同步模式
        data: {},
        success:function(result){
            for(var count = 0; count < result.length; count++){
                var temp = new RankInfo(result[count].employeeId, result[count].employeeName, result[count].judgement.grade, count);
                rank.push(temp);
            }
            for(var count = 0; count < rank.length; count++){
                for(var count2 = 0; count2 < rank.length - 1; count2++){
                    if(rank[count2].employerData < rank[count2+1].employerData){
                        var temp = rank[count2];
                        rank[count2] = rank[count2+1];
                        rank[count2+1] = temp;
                    }
                }
            }
        },
        error:function(result){
            alert("error");
        }
    });
    for(var count =0; count < rank.length; count++){
        rank[count].employerRank = count+1;
    }
    if(rank.length<=10){
        return rank;
    }else{
        var res = [];
        for(var count=0; count<10;count++){
            res[count] = rank[count];
        }
        for(var count = 0; count < rank.length; count++){
            if(rank[count].employeeId == employeeId){
                res[10] = rank[count];
            }
        }
        return res;
    }
}

//获得该工人参与的任务的所有pictureType及其数量
//例如：参与任务1，类型为自然、动物，图片数量为100，
//      参与任务2，类型为自然、植物，图片数量为50
//      则返回{自然:150，动物:100，植物:50}
function getPictureType(employeeId){
    var result = [];
    var underwayTaskId = getUnderwayTasks(employeeId);
    var completedTaskId = getCompletedTasks(employeeId);
    var taskId = [];
    var taskTypeNum = [];
    for(var i = 0; i < underwayTaskId.length; i++)
        taskId.push(underwayTaskId[i]);
    for(var i = 0; i < completedTaskId.length; i++)
        taskId.push(completedTaskId[i]);
    for(var i = 0; i < taskId.length; i++){
        var store = getTaskType(taskId[i]);
        for(var j = 0; j < store.length; j++)
            taskTypeNum.push(store[j]);
    }
    for(var i = 0; i < taskTypeNum.length; i++){
        var sign = 0;
        for(var j = 0; j < result.length; j++){
            if(taskTypeNum[i].pictureType == result[j].pictureType)
                sign = 1;
        }
        if(sign == 0)
            result.push(taskTypeNum[i]);
    }
    for(var i = 0; i < result.length; i++){
        var sign = 0;
        for(var j = 0; j < taskTypeNum.length; j++){
            if(taskTypeNum[j].pictureType == result[i].pictureType) {
                if(sign == 0){
                    sign = 1;
                }else {
                    result[i].typeNum += taskTypeNum[j].typeNum;
                }
            }
        }
    }
    for(var i = 0; i < result.length; i++){
        for(var j = 0; j < result.length - 1; j++){
            if(result[j].typeNum < result[j+1].typeNum){
                var temp = result[j];
                result[j] = result[j+1];
                result[j+1] = temp;
            }
        }
    }
    return result;
}

function getUnderwayTasks(employeeId) {
    var taskId = [];
    $.ajax({
        type: 'POST',
        url:"/EmployeeTask/showUnderwayTasks",
        async: false,                         //将ajax改为同步模式
        data: {
            employeeId:employeeId
        },
        success:function(result){
            if(result.length > 0){
                for(var i = 0; i < result.length; i++)
                    taskId.push(result[i].taskId);
            }
        },
        error:function(result){
            alert("error");
        }
    });
    return taskId;
}

function getCompletedTasks(employeeId) {
    var taskId = [];
    $.ajax({
        type: 'POST',
        url:"/EmployeeTask/showCompletedTasks",
        async: false,                         //将ajax改为同步模式
        data: {
            employeeId:employeeId
        },
        success:function(result){
            if(result.length > 0){
                for(var i = 0; i < result.length; i++)
                    taskId.push(result[i].taskId);
            }
        },
        error:function(result){
            alert("error");
        }
    });
    return taskId;
}

function getTaskType(taskId) {
    var taskType = [];
    var pictureNum = 0;
    $.ajax({
        type: 'POST',
        url:"/EmployeeTask/checkOneTask",
        async: false,                         //将ajax改为同步模式
        data: {
            taskId:taskId
        },
        success:function(result){
            taskType = result.taskType;
            pictureNum = result.totalPictureAmount;
        },
        error:function(result){
            alert("error");
        }
    });
    var result = [];
    for(var i = 0; i < taskType.length; i++){
        var temp = new PictureTypeNum(taskType[i], pictureNum);
        result.push(temp);
    }
    return result;
}
//获得该工人参与三种发布方式任务的数量，返回TaskTypeInfo,见js开头
//注：记得把adminData里的返回所有任务的发布方式也修改掉（新加分块发布）！！！
function getReleaseType(employeeId){
    var result = new TaskTypeInfo(0,0,0);
    var underwayTaskId = getUnderwayTasks(employeeId);
    var completedTaskId = getCompletedTasks(employeeId);
    var taskId = [];
    for(var i = 0; i < underwayTaskId.length; i++)
        taskId.push(underwayTaskId[i]);
    for(var i = 0; i < completedTaskId.length; i++)
        taskId.push(completedTaskId[i]);
    for(var i = 0; i < taskId.length; i++){
        var type = getTaskReleaseType(taskId[i]);
        if(type == "APPOINTED")
            result.specifyTask++;
        if(type == "NON_APPOINTED")
            result.openTask++;
        if(type == "SHARED")
            result.cutPartTask++;
    }
    return result;
}

function getTaskReleaseType(taskId) {
    var res = "";
    $.ajax({
        type: 'POST',
        url:"/EmployeeTask/checkOneTask",
        async: false,                         //将ajax改为同步模式
        data: {
            taskId:taskId
        },
        success:function(result){
            res = result.releaseType;
        },
        error:function(result){
            alert("error");
        }
    });
    return res;
}