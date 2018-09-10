var dataArr2 = []; // 储存所选图片的结果(文件名和base64数据)
function read2(){
    var input2 = document.getElementById("file_input2");      
    var result;
    var fd;  //FormData方式发送请求      
    var oSelect2 = document.getElementById("select2");    
    var oAdd2 = document.getElementById("add2");    
    var oinput2 = document.getElementById("file_input2");    
	      
    if(typeof FileReader==='undefined'){      
        alert("抱歉，你的浏览器不支持 FileReader");      
        input2.setAttribute('disabled','disabled');      
    }else{      
        input2.addEventListener('change',readFile2,false);   		
    }　　　　　//handler      
      
          
    function readFile2(){     
        fd = new FormData();      
        var iLen = this.files.length;    
        var index = 0;    
        for(var i=0;i<iLen;i++){    
            if (!input2['value'].match(/.jpg|.gif|.png|.jpeg|.bmp/i)){　　//判断上传文件格式      
                return alert("上传的图片格式不正确，请重新选择");      
            }    
            var reader = new FileReader();    
            reader.index = i;      
            fd.append(i,this.files[i]);    
            reader.readAsDataURL(this.files[i]);  //转成base64      
            reader.fileName = this.files[i].name;    
    
    
            reader.onload = function(e){     
                var imgMsg = {
                    name : this.fileName,//获取文件名      
                    base64 : this.result   //reader.readAsDataURL方法执行完后，base64数据储存在reader.result里      
                };
                dataArr2.push(imgMsg);
                result = '<div class="delete">delete</div><div class="result"><img src="'+this.result+'" alt=""/></div>';      
                var div = document.createElement('div');    
                div.innerHTML = result;      
                div['className'] = 'float2';    
                div['index'] = index;      
                document.getElementsByClassName('panel-rightMiddle')[0].appendChild(div);  　　//插入dom树      
                var img = div.getElementsByTagName('img')[0];    
                img.onload = function(){      
                    var nowHeight = ReSizePic(this); //设置图片大小      
                    this.parentNode.style.display = 'block';      
                    var oParent = this.parentNode;      
                    if(nowHeight){      
                        oParent.style.paddingTop = (oParent.offsetHeight - nowHeight)/2 + 'px';      
                    }      
                };
    
    
                div.onclick = function(){    
                    this.remove();                  // 在页面中删除该图片元素    
                    delete dataArr2[this.index];  // 删除dataArr2对应的数据    
                        
                };
                index++;    
            }      
        }      
    }      
    
    
    oSelect2.onclick=function(){     
        oinput2.value = "";   // 先将oinput2值清空，否则选择图片与上次相同时change事件不会触发    
        //清空已选图片    
        $('.float2').remove();    
        dataArr2 = [];     
        index = 0;            
           
    };
    
	 
    
    oAdd2.onclick=function(){    
        oinput2.value = "";   // 先将oinput2值清空，否则选择图片与上次相同时change事件不会触发    
        oinput2.click();     
    }     
    
}      
/*     
 用ajax发送fd参数时要告诉jQuery不要去处理发送的数据，     
 不要去设置Content-Type请求头才可以发送成功，否则会报“Illegal invocation”的错误，     
 也就是非法调用，所以要加上“processData: false,contentType: false,”     
 * */      
      
                  
function ReSizePic(ThisPic) {      
    var RePicWidth = 50; //这里修改为您想显示的宽度值      
      
    var TrueWidth = ThisPic.width; //图片实际宽度      
    var TrueHeight = ThisPic.height; //图片实际高度      
          
    if(TrueWidth>TrueHeight){      
        //宽大于高      
        var reWidth = RePicWidth;      
        ThisPic.width = reWidth;      
        //垂直居中      
        var nowHeight = TrueHeight * (reWidth/TrueWidth);      
        return nowHeight;  //将图片修改后的高度返回，供垂直居中用      
    }else{      
        //宽小于高      
        var reHeight = RePicWidth;      
        ThisPic.height = reHeight;      
    }      
}      