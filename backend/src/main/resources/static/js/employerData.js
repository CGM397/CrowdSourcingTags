function RankInfo(userId,userName,employerData,employerRank){
    this.userId=userId;//用户账号
    this.userName=userName;//用户名
    this.employerData=employerData;//特定数据
    this.employerRank=employerRank;//数据排名
}
function TaskTypeInfo(openTask,specifyTask){
    this.openTask=openTask;//自由发布任务数
    this.specifyTask=specifyTask;//指派发布任务数
}

function PictureTypeNum(pictureType, typeNum){
    this.pictureType = pictureType;
    this.typeNum = typeNum;
}

//返回对象数组rankinfo[]；数组长度为11,前十位+自己;前十位根据完成任务数排名；
// 对象属性为发起者id、发起者用户名、发起者完成任务数、发起者排名；
function getEmployerCompletedTaskRank(employerId){
    var rank = [];
    $.ajax({
        type: 'POST',
        url:"/UserManagement/showEmployerInfo",
        async: false,                         //将ajax改为同步模式
        data: {},
        success:function(result){
            for(var count = 0; count < result.length; count++){
                var temp = new RankInfo(result[count].employerId, result[count].employerName, result[count].taskCompleted, count);
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
            if(rank[count].employerId == employerId){
                res[10] = rank[count];
            }
        }
        return res;
    }
}

//返回对象数组rankinfo[]；数组长度为11,前十位+自己;前十位根据发布任务数排名；
// 对象属性为发起者id、发起者用户名、发起者发布任务数、发起者排名；
function getEmployerReleaseTaskRank(employerId){
    var rank = [];
    $.ajax({
        type: 'POST',
        url:"/UserManagement/showEmployerInfo",
        async: false,                         //将ajax改为同步模式
        data: {},
        success:function(result){
            for(var count = 0; count < result.length; count++){
                var temp = new RankInfo(result[count].employerId, result[count].employerName, result[count].taskAmount, count);
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
            if(rank[count].employerId == employerId){
                res[10] = rank[count];
            }
        }
        return res;
    }
}

//返回对象TaskType;对象属性为该发起者总计自由发布任务数量和指派发布任务数量；
function getTaskTypeInfo(employerId){
    var taskId = [];
    var taskType = new TaskTypeInfo(0 ,0);
    var underwayId = getUnderwayTasksId(employerId);
    var completedId = getCompletedTasksId(employerId);
    for(var count=0; count<underwayId.length; count++){
        taskId.push(underwayId[count]);
    }
    for(var count=0; count<completedId.length; count++){
        taskId.push(completedId[count]);
    }
    for(var count=0; count<taskId.length; count++){
        var type = getOneTaskType(taskId[count]);
        if(type == "APPOINTED"){
            taskType.specifyTask++;
        }else{
            taskType.openTask++;
        }
    }
    return taskType;
}

function getUnderwayTasksId(employerId) {
    var underwayId = [];
    $.ajax({
        type: 'POST',
        url:"/EmployerTask/showUnderwayTasks",
        async: false,                         //将ajax改为同步模式
        data: {
            employerId:employerId
        },
        success:function(result){
            for(var count = 0; count < result.length; count++){
                underwayId.push(result[count].taskId);
            }
        },
        error:function(result){
            alert("error");
        }
    });
    return underwayId;
}

function getCompletedTasksId(employerId) {
    var completedId = [];
    $.ajax({
        type: 'POST',
        url:"/EmployerTask/showCompletedTasks",
        async: false,                         //将ajax改为同步模式
        data: {
            employerId:employerId
        },
        success:function(result){
            for(var count = 0; count < result.length; count++){
                completedId.push(result[count].taskId);
            }
        },
        error:function(result){
            alert("error");
        }
    });
    return completedId;
}

function getOneTaskType(taskId) {
    var type = "";
    $.ajax({
        type: 'POST',
        url:"/EmployerTask/checkOneTask",
        async: false,                         //将ajax改为同步模式
        data: {
            taskId:taskId
        },
        success:function(result){
            type = result.releaseType;
        },
        error:function(result){
            alert("error");
        }
    });
    return type;
}

//获得该发起者发布的任务的所有pictureType及其数量
//例如：发布任务1，类型为自然、动物，图片数量为100，
//      发布任务2，类型为自然、植物，图片数量为50
//      则返回{自然:150，动物:100，植物:50}
function getPictureType(employerId){
    var result = [];
    var underwayTaskId = getEmployerUnderwayTasks(employerId);
    var completedTaskId = getEmployerCompletedTasks(employerId);
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
            if(taskTypeNum[j].pictureType == result[i].pictureType){
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
                var temp = result[i];
                result[i] = result[i+1];
                result[i+1] = temp;
            }
        }
    }
    return result;
}

function getEmployerUnderwayTasks(employerId) {
    var taskId = [];
    $.ajax({
        type: 'POST',
        url:"/EmployerTask/showUnderwayTasks",
        async: false,                         //将ajax改为同步模式
        data: {
            employerId:employerId
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

function getEmployerCompletedTasks(employerId) {
    var taskId = [];
    $.ajax({
        type: 'POST',
        url:"/EmployerTask/showCompletedTasks",
        async: false,                         //将ajax改为同步模式
        data: {
            employerId:employerId
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