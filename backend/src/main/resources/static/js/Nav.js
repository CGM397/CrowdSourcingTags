$(function (){
    $('#nky li').click(function (){
        //把之前已有的active去掉
        $('.active').removeClass('active');
        //当前点击的li加上class
        $(this).addClass("active");

    });
})