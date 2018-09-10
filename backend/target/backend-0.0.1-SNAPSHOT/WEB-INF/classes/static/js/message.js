var newMessage=[];
var readMessage=[];
function Message(messageId,userId,releaseTime,title,content,isRead){
    this.messageId=messageId;
    this.userId=userId;
    this.releaseTime=releaseTime;
    this.title=title;
    this.content=content;
    this.isRead=isRead;
}
function getReadMessage(){
    var mode=(document.getElementById('read').innerText=='全部标为已读')?'未读':'已读';
    var userId=localStorage.getItem('userId');
    //ajax获得已读
    $.ajax({
        type: 'POST',
        url:"/Message/getReadMessage",
        async: false,
        data: {
            userId:userId
        },
        success:function(result){
           readMessage=result;
        },
        error:function(result){
            alert("error");
        }
    });
}
function getNewMessage(){
    var userId=localStorage.getItem('userId');
    //ajax获得未读
    $.ajax({
        type: 'POST',
        url:"/Message/getNewMessage",
        async: false,
        data: {
            userId:userId
        },
        success:function(result){
            newMessage=result;
        },
        error:function(result){
            alert(result);
        }
    });
}
function refreshNew(){
    deleteAllMessage();
    getNewMessage();
    for(var i=0;i<newMessage.length;i++){
        addMessage(i,newMessage[i]);
    }
}
function refreshRead(){
    deleteAllMessage();
    getReadMessage();
    for(var i=0;i<readMessage.length;i++){
        addMessage(i,readMessage[i]);
    }
}
function readOneMessage(index){
    var mode=(document.getElementById('read').innerText=='全部标为已读')?'未读':'已读';
    var userId=localStorage.getItem('userId');
    var message;
    if(mode=='已读'){
        message=readMessage[index];
    }else{
        message=newMessage[index];
    }
    //swal();
    $.ajax({
        type: 'POST',
        url:"/Message/readMessage",
        async: false,
        data: {
            userId:userId,
            messageId:message.messageId
        },
        success:function(result){
            var tasld="";
            var title=message.title;
            var text=message.content;
            var loader,cancel,confirmText;
            if(title=='欢迎新用户'){
                loader=false;
                cancel=false;
                confirmText="确认";
            }
            else if(title=='入门考试通知') {
                loader = false;
                cancel = true;
                confirmText = "去考试";
            }
            else if(title=='新任务发布'||title=='新指派任务') {
                loader = false;
                cancel = true;
                confirmText = "去看看";
            }
            else if(title=='任务完成') {
                loader = true;
                cancel = true;
                confirmText = "领取";
                taskId=text.substr(5,19);
            }
            swal({
                    title: title,
                    text: text,
                    showCancelButton: cancel,
                    closeOnConfirm: false,
                    showLoaderOnConfirm: loader,
                    cancelButtonText: "取消",
                    confirmButtonText: confirmText,
                    animation: "slide-from-top",
                },
                function(){
                    if(title=='欢迎新用户') {
                        swal.close();
                    }
                    else if(title=='入门考试通知'){
                        setTimeout(function () {
                            window.location.href = 'employee-exam';
                            window.event.returnValue = false;
                        }, 2000);
                    }else if(title=='新任务发布'||title=='新指派任务'){
                        setTimeout(function () {
                            window.location.href = 'employee-newTask';
                            window.event.returnValue = false;
                        }, 2000);
                    }else if(title=='任务完成'){
                        setTimeout(function () {
                            $.ajax({
                                type: 'POST',
                                url:"/EmployeeTask/getAward",
                                data: {
                                    employeeId:userId,
                                    taskId:taskId
                                },
                                success:function(result){
                                    swal("领取成功","","success");
                                },
                                error:function(result){
                                    alert("error");
                                }
                            });
                        }, 2000);
                    }
                });
        },
        error:function(result){
            alert("error");
        }
    });
    if(mode=='已读'){
        refreshRead();
    }else{
        refreshNew();
    }
}



function deleteOneMessage(index){
    var mode=(document.getElementById('read').innerText=='全部标为已读')?'未读':'已读';
    var userId=localStorage.getItem('userId');
    var message;
    if(mode=='已读'){
        message=readMessage[index];
    }else{
        message=newMessage[index];
    }
    //获得id，调用ajax
    $.ajax({
        type: 'POST',
        url:"/Message/deleteMessage",
        async: false,
        data: {
            userId:userId,
            messageId:message.messageId
        },
        success:function(result){
            //swal;
            alert("success");
        },
        error:function(result){
            alert("error");
        }
    });
    if(mode=='已读'){
        refreshRead();
    }else{
        refreshNew();
    }
}
function readAll(){
    var userId=localStorage.getItem('userId');
    $.ajax({
        type: 'POST',
        url:"/Message/readAllMessage",
        async: false,
        data: {
            userId:userId
        },
        success:function(result){
            alert("success");
        },
        error:function(result){
            alert("error");
        }
    });
    //ajax
    refreshNew();
}
function deleteRead(){
    var userId=localStorage.getItem('userId');
    //ajax
    $.ajax({
        type: 'POST',
        url:"/Message/deleteReadMessage",
        async: false,
        data: {
            userId:userId
        },
        success:function(result){
            alert("success");
        },
        error:function(result){
            alert("error");
        }
    });
    refreshRead();
}
//新增一个dom消息
function addMessage(index,message){
    var messageBox=document.getElementById('messageBox');
    var newMessage=document.createElement('div');
    newMessage.className='txt';
    var label=document.createElement('label');
    label.className='Mlabel';
    label.innerText='标题:';
    var title=document.createElement('input');
    title.className='substance';
    title.type='text';
    title.placeholder=message.title;
    var time=document.createElement('input');
    time.className='date';
    time.type='text';
    time.placeholder=message.releaseTime.substr(0,10);
    var buttonDiv=document.createElement('div');
    buttonDiv.index=index;
    var check=document.createElement('a');
    check.className='checkBtn';
    check.innerText='查看';
    check.onclick=function(){
        readOneMessage(this.parentNode.index);
    };
    var deleteM=document.createElement('a');
    deleteM.className='deleteBtn';
    deleteM.innerText='删除';
    deleteM.onclick=function(){
        deleteOneMessage(this.parentNode.index);
    };
    buttonDiv.appendChild(check);
    buttonDiv.appendChild(deleteM);
    newMessage.appendChild(label);
    newMessage.appendChild(title);
    newMessage.appendChild(time);
    newMessage.appendChild(buttonDiv);
    messageBox.append(newMessage);
}
//删除所有dom消息
function deleteAllMessage(){
    var messageBox=document.getElementById('messageBox');
    while(messageBox.hasChildNodes()) //当elem下还存在子节点时 循环继续
    {
        messageBox.removeChild(messageBox.firstChild);
    }
}
function readButton(){
    if(document.getElementById('read').innerText=='全部标为已读'){
        readAll();
    }
    else{
        deleteRead();
    }
}
function openM(){
    if(document.getElementById('messageP').className=='panelM') {
        document.getElementById('messageP').className = 'panelM none';
    }
    else {
        document.getElementById('messageP').className = 'panelM';
        refreshNew();
    }
}

//var message=new Message('1','employee','2018/06/06 18:20:21','新任务发布','该任务为自由发布任务,类型：自然/植物，奖励100','0');
//addMessage(1,message);
