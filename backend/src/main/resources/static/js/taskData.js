function TaskChange(accept,finish){
    this.accept=accept;
    this.finish=finish;
}

//返回从任务发起到现在每一天的接收总数和完成总数
//例如：7天前任务发起，现在接收6，完成2
//accept=[0,1,3,4,4,6,6],complete=[0,0,1,1,2,2,2]
function getTaskChange(taskId){
    var accept = [];
    var finish = [];
    var taskStartTime = "";
    var employerId = "";
    var day = 24 * 60 * 60 *1000;
    var checkCurTime = new Date();
    var curTime = checkCurTime.getTime();
    var acceptTime = [];
    var finishTime = [];
    $.ajax({
        type: 'POST',
        url:"/EmployeeTask/checkOneTask",
        async: false,                         //将ajax改为同步模式
        data: {
            taskId:taskId
        },
        success:function(result){
            taskStartTime = result.startTime;
            employerId = result.employerId;
        },
        error:function(result){
            alert("error");
        }
    });
    var checkStartTime = new Date();
    checkStartTime.setFullYear(parseInt(taskStartTime.substr(0,4)), parseInt(taskStartTime.substr(5,2))-1,
                               parseInt(taskStartTime.substr(8,2)));
    checkStartTime.setHours(0,0,0,0);
    var startTime = checkStartTime.getTime();
    var totalTime = (curTime - startTime)/day + 1;
    var employeeIds = getEmployeeIds(employerId, taskId);
    for(var i = 0; i < employeeIds.length; i++){
        var temp = getOneEmployeeTaskTime(employeeIds[i],taskId);
        acceptTime.push(temp[0]);
        if(temp.length>1)
            finishTime.push(temp[1]);
    }
    accept[0] = 0;
    finish[0] = 0;
    for(var i = 1; i <= totalTime; i++){
        accept[i] = 0;
        finish[i] = 0;
        for(var j = 0; j < acceptTime.length; j++){
            if(acceptTime[j] <= curTime && (acceptTime[j] - startTime)/day < i)
                accept[i]++;
        }
        for(var k = 0; k < finishTime.length; k++){
            if(finishTime[k] <= curTime && (finishTime[k] - startTime)/day < i)
                finish[i]++;
        }
    }
    return new TaskChange(accept, finish);
}

function getEmployeeIds(employerId, taskId) {
    var employeeIds = [];
    $.ajax({
        type: 'POST',
        url:"/EmployerTask/showOneTaskInfo",
        async: false,                         //将ajax改为同步模式
        data: {
            employerId:employerId,
            taskId:taskId
        },
        success:function(result){
            employeeIds = result.acceptedEmployeeId;
        },
        error:function(result){
            alert("error");
        }
    });
    return employeeIds;
}

function getOneEmployeeTaskTime(employeeId, taskId) {
    var taskTime = [];
    $.ajax({
        type: 'POST',
        url:"/EmployeeTask/showOneTaskInfo",
        async: false,                         //将ajax改为同步模式
        data: {
            employeeId:employeeId,
            taskId:taskId
        },
        success:function(result){
            var time1 = new Date();
            var time2 = new Date();
            time1.setFullYear(parseInt(result.acceptTime.substr(0,4)),
                              parseInt(result.acceptTime.substr(5,2))-1,
                              parseInt(result.acceptTime.substr(8,2)));
            time1.setHours(0,0,0,0);
            if(result.finishTime!="finishTime"){
                time2.setFullYear(parseInt(result.finishTime.substr(0,4)),
                    parseInt(result.finishTime.substr(5,2))-1,
                    parseInt(result.finishTime.substr(8,2)));
                time2.setHours(0,0,0,0);
                taskTime[1] = time2.getTime();
            }
            taskTime[0] = time1.getTime();
        },
        error:function(result){
            alert("error");
        }
    });
    return taskTime;
}