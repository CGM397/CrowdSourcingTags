$("#save").click(function(){
    var Coordinate = {
        x:1,
        y:2
    };
    var border = [Coordinate,Coordinate];
    var tempAnnotation = document.getElementById("description");
    var annotation = tempAnnotation.value;
    var data = {
        pictureID: file.name,
        tagID: "001",
        border:border,
        annotation: annotation
    };
    $.ajax({
        type: 'POST',
        url:"/savePictureTag",
        contentType: "application/json",
        data: JSON.stringify(data),
        success:function(result){
            alert("success!");
        },
        error: function () {
            alert("error!");
        }
    });
});