function RankInfo(userId,userName,employerData,employerRank){
    this.userId=userId;//用户账号
    this.userName=userName;//用户名
    this.employerData=employerData;//特定数据
    this.employerRank=employerRank;//数据排名
}
function AnnotationTypeInfo(overall,rect,bound){
    this.overall=overall;//自由发布任务数
    this.rect=bound;//指派发布任务数
}
function TaskTypeInfo(openTask,specifyTask,cutPartTask){
    this.openTask=openTask;//自由发布任务数
    this.specifyTask=specifyTask;//指派发布任务数
    this.cutPartTask=cutPartTask;//分块发布任务数
}
function TaskConditionInfo(underway,completed){
    this.underway=underway;//进行中任务数
    this.completed=completed;//已完成任务数
}
function UserNum(employerNum,employeeNum){
    this.employerNum=employerNum;//众包发起者数量
    this.employeeNum=employeeNum;//众包工人数量
}
function CompletedChange(finishTime,finishNum){
    this.finishTime=finishTime;//任务完成时间
    this.finishNum=finishNum;//已完成任务数量
}

//返回UserNum,对象属性为众包发起者人数、众包工人数
function getUserNum(){
    var employerNum=0;
    var employeeNum=0;
    $.ajax({
        type: 'POST',
        url:"/UserManagement/showEmployerInfo",
        async: false,                         //将ajax改为同步模式
        data: {},
        success:function(result){
            employerNum = result.length;
        },
        error:function(result){
            alert("error");
        }
    });
    $.ajax({
        type: 'POST',
        url:"/UserManagement/showEmployeeInfo",
        async: false,                         //将ajax改为同步模式
        data: {},
        success:function(result){
            employeeNum = result.length;
        },
        error:function(result){
            alert("error");
        }
    });
    var userNum = new UserNum(employerNum, employeeNum);
    return userNum;
}

function RankInfo(userId,userName,employerData,employerRank){
    this.userId=userId;//用户账号
    this.userName=userName;//用户名
    this.employerData=employerData;//特定数据
    this.employerRank=employerRank;//数据排名
}

function getCompletedTaskRank(){
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
        return res;
    }
}
function getEmployerReleaseTaskRank(){
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
        return res;
    }
}
function getEmployeeCompletedTaskRank(){
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
        return res;
    }
}
function getEmployeeAwardRank(){
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
        return res;
    }
}

//返回对象TaskConditionInfo;对象属性为已完成任务数和进行中任务数
function getTaskCondition(){
    var taskConditionInfo=new TaskConditionInfo(0,0);
    $.ajax({
        type: 'POST',
        url:"/UserManagement/showEmployerInfo",
        async: false,                         //将ajax改为同步模式
        data: {},
        success:function(result){
            for(var count = 0; count < result.length; count++){
                var oneEmployerId = result[count].employerId;
                taskConditionInfo.underway+=getUnderwayTasksNum(oneEmployerId);
                taskConditionInfo.completed+=getCompletedTasksNum(oneEmployerId);
            }
        },
        error:function(result){
            alert("error");
        }
    });
    return taskConditionInfo;
}

function getUnderwayTasksNum(employerId) {
    var underwayNum = 0;
    $.ajax({
        type: 'POST',
        url:"/EmployerTask/showUnderwayTasks",
        async: false,                         //将ajax改为同步模式
        data: {
            employerId:employerId
        },
        success:function(result){
            underwayNum = result.length;
        },
        error:function(result){
            alert("error");
        }
    });
    return underwayNum;
}

function getCompletedTasksNum(employerId) {
    var completedNum = 0;
    $.ajax({
        type: 'POST',
        url:"/EmployerTask/showCompletedTasks",
        async: false,                         //将ajax改为同步模式
        data: {
            employerId:employerId
        },
        success:function(result){
            completedNum = result.length;
        },
        error:function(result){
            alert("error");
        }
    });
    return completedNum;
}

//返回对象TaskType；象属性为自由发布任务数量和指派发布任务数量；
function getTaskTypeInfo(){
    var taskType = new TaskTypeInfo(0, 0, 0);
    $.ajax({
        type: 'POST',
        url:"/UserManagement/showEmployerInfo",
        async: false,                         //将ajax改为同步模式
        data: {},
        success:function(result){
            for(var count = 0; count < result.length; count++){
                var oneEmployerId = result[count].employerId;
                var underwayId = getUnderwayTasksId(oneEmployerId);
                var completedId = getCompletedTasksId(oneEmployerId);
                for(var count2 = 0; count2 < underwayId.length; count2++){
                    var type = getOneTaskType(underwayId[count2]);
                    if(type == "APPOINTED"){
                        taskType.specifyTask++;
                    }else if(type == "NON_APPOINTED"){
                        taskType.openTask++;
                    }else{
                        taskType.cutPartTask++;
                    }
                }
                for(var count3 = 0; count3 < completedId.length; count3++){
                    var type = getOneTaskType(completedId[count3]);
                    if(type == "APPOINTED"){
                        taskType.specifyTask++;
                    }else{
                        taskType.openTask++;
                    }
                }
            }
        },
        error:function(result){
            alert("error");
        }
    });
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

//返回对象数组CompletedChange[]；对象属性为完成时间和此时的完成任务数
function getCompletedTaskChange(){
    var completedTime = [];
    var completedChange = [];
    var first = new CompletedChange(0, 0);
    completedChange.push(first);
    $.ajax({
        type: 'POST',
        url:"/UserManagement/showEmployeeInfo",
        async: false,                         //将ajax改为同步模式
        data: {},
        success:function(result){
            for(var count = 0; count < result.length; count++){
                var oneEmployeeId = result[count].employeeId;
                var finish = employeeCompletedTime(oneEmployeeId);
                for(var count2 = 0; count2 < finish.length; count2++){
                    completedTime.push(finish[count2]);
                }
            }
        },
        error:function(result){
            alert("error");
        }
    });
    for(var count = 0; count < completedTime.length; count++){
        for(var i = 0; i < completedTime.length - 1; i++){
            if(completedTime[i] > completedTime[i+1]){
                var tmp = completedTime[i];
                completedTime[i] = completedTime[i+1];
                completedTime[i+1] = tmp;
            }
        }
    }
    var day = 24 * 60 * 60 *1000;
    var firstTime = completedTime[0]+"";
    var checkDate = new Date();
    checkDate.setFullYear(parseInt(firstTime.substr(0, 4)), parseInt(firstTime.substr(4, 2))-1, parseInt(firstTime.substr(6, 2)));
    checkDate.setHours(0,0,0,0);
    var checkTime = checkDate.getTime();
    for(var count = 0; count < completedTime.length; count++){
        var t = completedTime[count]+"";
        var checkDate2 = new Date();
        checkDate2.setFullYear(parseInt(t.substr(0,4)), parseInt(t.substr(4,2))-1, parseInt(t.substr(6,2)));
        checkDate2.setHours(0,0,0,0);
        var checkTime2 = checkDate2.getTime();
        var cha = (checkTime2 - checkTime)/day;
        var store = new CompletedChange(cha+1, count+1);
        while(count<completedTime.length - 1 && completedTime[count] == completedTime[count+1]){
            store.finishNum++;
            count++;
        }
        completedChange.push(store);
    }
    return completedChange;
}

function employeeCompletedTime(employeeId) {
    var completedTime = [];
    $.ajax({
        type: 'POST',
        url:"/EmployeeTask/showCompletedTasks",
        async: false,                         //将ajax改为同步模式
        data: {
            employeeId:employeeId
        },
        success:function(result){
            for(var count = 0; count < result.length; count++){
                var finishTime = [];
                finishTime = result[count].finishTime.split(" ");
                var finish = finishTime[0].split("-");
                var store = finish[0]+""+finish[1]+""+finish[2];
                var timeNum = parseInt(store);
                completedTime.push(timeNum);
            }
        },
        error:function(result){
            alert("error");
        }
    });
    return completedTime;
}

var annotationJudgement = {
    accuracy:0,
    efficiency:0,
    grade:0
};

var employeeInfo = {
    employeeName:"",
    employeeId:"",
    employeePassword:"",
    employeeEmail:"",
    dpId:"",
    awardAmount:0,
    taskAmount:0,
    taskUnderway:0,
    taskCompleted:0,
    judgement:annotationJudgement
};

var employerInfo = {
    employerName:"",
    employerId:"",
    employerPassword:"",
    employerEmail:"",
    dpId:"",
    taskAmount:0,
    taskUnderway:0,
    taskCompleted:0
};