// jmtxwzyjt qsjdhmwmdcp ywwmrxh np
var websocket;
var exist = false;
var connect = false;
var myInfo = {};
var users = [];
var sellerInfo = {};
var sellerId = $("#thoseflag").val()

Array.prototype.contains = function (needle) {
    for (let i in this) {
        let _key = this[i];
        if (undefined != _key && _key == needle)
            return true;
    }
    return false;

}

//init
$(function () {
    $(".sendit").click(function () {
        let content = $("#sendContent").val();
        if (content.trim().length < 1) {
            $("#sendContent").attr("placeholder", "发送内容不能为空")
            $("#sendContent").val("")
            return
        } else {
            $("#sendContent").attr("placeholder", "请输入内容")
        }
        let id = $(".user_list .active").attr("id");
        let center = $("#flag" + id);
        let html = "<div class='right clearfloat'><div class='time'>" + dataForm() + "</div><img src='" + usavar + "'/><span class='msg'>" + content +
            "</span></div>";
        center.append(html);
        center.scrollTop(center[0].scrollHeight);
        let data = {}
        data.msg = content;
        data.time = new Date()
        data.userId = userId
        data.sellerId = id;
        data = JSON.stringify(data);
        sendMsg(data)
        $("#sendContent").val("")
    });
});

function dataForm() {
    let nowYear = new Date().getFullYear().toString()
    let nowMonth = (new Date().getMonth() + 1).toString()
    let nowDay = new Date().getDate().toString();
    let nowHours = new Date().getHours().toString(); //获取当前小时数(0-23)
    let nowMin = new Date().getMinutes().toString(); //获取当前分钟数(0-59)
    let nowSeconds = new Date().getSeconds().toString(); //获取当前秒数(0-59)
    function timeAdd0(str) {
        if (str.length <= 1) {
            str = '0' + str;
        }
        return str
    }

    nowYear = timeAdd0(nowYear)
    nowMonth = timeAdd0(nowMonth)
    nowDay = timeAdd0(nowDay)
    nowHours = timeAdd0(nowHours)
    nowMin = timeAdd0(nowMin)
    nowSeconds = timeAdd0(nowSeconds)
    return nowYear + "/" + nowMonth + "/" + nowDay + "   " + nowHours + ":" + nowMin + ":" + nowSeconds
}

/*用于校验是否在线离线的作用*/
function checkAlive(alive, types) {
    if (0 == types) {
        $("._alive").remove()
        let aliveList = [];
        for (let key in alive) {
            aliveList.push(key)
        }

        $(".user_list li").each(function () {
            if (aliveList.contains(this.id)) {
                $(this).find("span:first").append("<span class='_alive' types='1' >(在线)</span>")
            } else {
                $(this).find("span:first").append("<span class='_alive' types='0'>(离线)</span>")
            }
        })
    } else {
        let tid = alive.userId;
        $(".user_list li").each(function () {
            let thisSpan = $(this).find("._alive");
            if (tid == this.id) {
                if (3 == types) {
                    $(thisSpan).text("(在线)")
                } else {
                    $(thisSpan).text("(离线)")
                }
            }
        })

    }
}

/*h5的推送功能*/
function showMess(msg, other) {

    if (undefined != msg && msg.indexOf("亲") == -1) {
        //如果支持window.Notification 并且 许可不是拒绝状态
        if (window.Notification && Notification.permission !== "denied") {
            //Notification.requestPermission这是一个静态方法，作用就是让浏览器出现是否允许通知的提示
            Notification.requestPermission(function (status) {
                //如果状态是同意
                if (status === "granted") {
                    var m = new Notification('收到信息', {
                        body: msg, //消息体内容
                        icon: other.userAvar //消息图片
                    });
                    m.onclick = function () { //点击当前消息提示框后，跳转到当前页面
                        $(".show").click()
                    }
                }
            });
        }
    }
}


/*历史聊天记录展示的窗口*/
var currentpage = 0;
var totalSize = 0;
var history_open = false;
var currentHeight = 0;


$(".history_list_content").scroll(function () {
    let curHeight = $(".history_list_content").scrollTop();
    if (currentHeight == curHeight) {
        let rest = totalSize - (currentpage + 1) * 10
        if (rest > 0) {
            console.log("下一页")
            currentpage ++;
            refreshHistoryRecode();
        }else{
            console.log("最后一页")
        }
    }
})

//关闭窗口
function closeHistoryList() {
    $("#closeHistoryList").hide();
    currentpage = 0;
    totalSize = 0;
    history_open = false;
}

//打开窗口
function openHistoryList() {
    if (history_open) {
        closeHistoryList()
        return
    } else {
        history_open = true;
    }
    $("#closeHistoryList").show();
    $(".history_list_content li").remove()
    refreshHistoryRecode();
}

//获取记录数据
function refreshHistoryRecode() {
    let pojo = {}
    pojo.pid = $(".user_list .active").attr("id");
    pojo.page = currentpage;

    let curImg = $(".user_list .active img").attr("src")
    let curNick = $(".user_list .active span").text();
    if (curNick.length > 0) {
        curNick = curNick.split("(")[0]
    }
    $(".seller_name").text(curNick)
    $.doAjaxJSON(pojo, "./chat/history.html", function (data) {
        let msgArry = data.data;
        totalSize = data.total
        if (msgArry.length < 1) {
            return
        }
        for (var i = 0, len = msgArry.length; i < len; i++) {

            let majo = JSON.parse(msgArry[i])
            let _nick = curNick;
            let _curImg = curImg;
            if (userId == majo.userId) {
                _nick = ''
                _curImg = usavar
            }
            let myhtml = '<li class="clearfloat"><div class="avatar">' +
                '<img src="' + _curImg + '"/></div>' +
                '<div class="content"> <div class="content_top">' +
                '<span>' + _nick + '</span>' +
                '<span class="date">' + new Date(majo.time).toLocaleDateString() + ' ' + new Date(majo.time).toLocaleTimeString() + '</span></div>' +
                '<div class="content_main">' + majo.msg + '</div></div></li>'
            $(".history_list_content ul").append(myhtml)
        }
        currentHeight = $(".history_list_content").find("ul").height() - $(".history_list_content").height();
        ;
    })
}


/*reconnect the server*/
$(document).on("click", '.retry', function () {
    $(".retry").remove()
    initSocket()
})

/*监听发送信息*/
$('#sendContent').bind('keyup', function (event) {
    if (event.keyCode == 13) {
        let id = $(".user_list .active").attr("id");
        sellerId = id;
        $(".sendit").click();
    }
});

/*统一新信息展示窗口*/
function tips(msg, img, id, date) {
    let imgs = "huaji.png"
    let flag = $(".user_list .active").attr("id");
    let data = dataForm()
    if (undefined != id) {
        flag = id
        let imgss = $("#" + id).find("img").attr("src")
        if (undefined != imgss) {
            imgs = imgss;
        }
    }
    if (undefined != img) {
        imgs = img
    }
    if (undefined != date) {
        data = date.replace("T", " ")
    }
    let center = $("#flag" + flag)
    let html = "<div class='left clearfloat'><span>" + data + "</span><img width='50' height='50' src='" + imgs + "'/><span class='msg'>" + msg + "</a></span></div>";
    $("#flag" + flag).append(html);
    center.scrollTop(center[0].scrollHeight);
    $(".chat_box .chat_content").hide();
    $("#flag" + flag).show()
    $(".user_list li").removeClass("active");
    $("#" + flag).addClass("active");
}

function initSocket() {

    if (window.WebSocket) {
        connect = false;
        websocket = new WebSocket("ws://www.coffeeandice.cn:8082/ws");

        //当有消息过来的时候触发
        websocket.onmessage = function (event) {
            if (!connect) {
                let data = JSON.parse(event.data)
                let msgTypes = data.msgType;
                if (1 == msgTypes) {
                    let maps = JSON.parse(data.data)

                    sellerInfo = maps;

                    let outRelate = JSON.parse(data.relateNet)
                    myInfo = JSON.parse(outRelate.INFO);
                    delete outRelate.INFO;

                    for (let key in outRelate) {
                        let pojo = JSON.parse(outRelate[key]);
                        users.push(pojo)
                        newChat(pojo)
                    }
                    newChat(maps)

                    tips("连接成功")
                    if (data.offLine.length > 0 && data.offLine != "{}") {
                        let offLine = JSON.parse(data.offLine)
                        let _offLine = [];
                        for (let key in offLine) {
                            if (undefined != offLine[key]) {
                                let pojo = JSON.parse(offLine[key]);
                                pojo.time = key;
                                _offLine.push(JSON.stringify(pojo))
                            }
                        }
                        _offLine = _offLine.reverse();
                        for (let ke = 0, len = _offLine.length; ke < len; ke++) {
                            if (undefined != _offLine[ke]) {
                                let pojo = JSON.parse(_offLine[ke]);
                                tips(pojo.msg, null, pojo.userId, pojo.time)
                            }
                        }
                    }
                    connect = true
                    if (data.onLine != "{}") {
                        let jo = JSON.parse(data.onLine)
                        checkAlive(jo, 0)
                    }
                    return
                } else {
                    let jo = JSON.parse(data.data)
                    checkAlive(jo, msgTypes)
                }
            }
            try {
                let data = JSON.parse(event.data)
                let msgTypes = data.msgType;
                if (2 == msgTypes) {
                    let other = JSON.parse(data.data)
                    newChat(other, 1)
                    tips(data.msg, other.sellerImg, other.userId)
                    showMess(data.msg, other)
                } else {
                    let jo = JSON.parse(data.data)
                    checkAlive(jo, msgTypes)
                }
            } catch (e) {
                tips(event.data)
            }

        }

        //连接关闭的时候触发
        websocket.onclose = function (event) {
            exist = false;
            if (connect) {
                let center = $(".chat_content");
                let html = "";
                html = "<div class='left clearfloat'><span>" + dataForm() + "</span><img width='50' height='50' src='huaji.png'/><span class='msg'>由于您长时间不在线，暂时断开连接！ <a href='javascript:void(0)' class='retry' >重新连接！</a></span></div>";
                $(".chat_content").append(html);
                center.scrollTop(center[0].scrollHeight);
            } else {
                let center = $(".chat_content");
                let html =
                    "<div class='left clearfloat'><span>" + dataForm() + "</span><img width='50' height='50' src='huaji.png'/><span class='msg'>暂时无法连接服务器,请！ <a href='javascript:void(0)' class='retry' >重新连接！</a></span></div>";
                $(".chat_content").append(html);
                center.scrollTop(center[0].scrollHeight);
            }
        }

        //连接打开的时候触发
        websocket.onopen = function (event) {
            exist = true;
            let respMessage = document.getElementById("respMessage");
            // respMessage.value = "建立连接";
            let data = {}
            data.msg = "heart";
            data.time = new Date()
            data.userId = userId;
            data.sellerId = sellerId;
            data = JSON.stringify(data);
            sendMsg(data)
        }
    } else {
        alert("浏览器不支持WebSocket");
    }
}

function sendMsg(msg) { //sent msg
    if (window.WebSocket && exist) {
        if (websocket.readyState == WebSocket.OPEN) {
            websocket.send(msg); //
        }
    } else {
        let center = $(".chat_content");
        let html = "<div class='left'><img width='50' height='50' src='huaji.png'/><span class='msg'>暂时无法连接服务器,请！ <a href='javascript:void(0)' class='retry' >重新连接！</a></span></div>";
        $(".chat_content").append(html);
        center.scrollTop(center[0].scrollHeight);
        return;
    }
}

function newChat(user, online) {
    let normal = $("#" + user.userId);
    if (normal.length < 1) {
        $(".user_list li").removeClass("active");
        $(".chat_box .chat_content").hide();
        let html = "<li class='active' id='" + user.userId + "'><img src='" + user.sellerImg + "'/>" +
            "<span>" + user.sellerName + ""
        if (undefined != online) {
            html += "<span class='_alive' types='1' >(在线)</span>"
        }
        html += "</span><a href='javascript:;'><span class='glyphicon glyphicon-remove'></span></a></li>"
        let contentmsg = "<div class='chat_content' id='flag" + user.userId + "'style='display: block;' ></div>"
        $(".chat_box").append(contentmsg)
        $(".user_list").append(html)
    } else {
        $(".user_list li").removeClass("active");
        $(normal).addClass("active");
    }
}


//点击用户名切换
$(document).on("click", ".user_list li", function () {
    $(".user_list li").removeClass("active");
    $(this).addClass("active");
    var index = $(this).index();
    $(".chat_box .chat_content").hide();
    $(".chat_box .chat_content").eq(index).show();
})

//点击删除
$(document).on("click", ".user_list li a", function () {
    //如果删除的节点为当前active
    if ($(this).parent("li").hasClass("active")) {
        //清除当前选中节点
        var index = $(this).parent("li").index();
        $(".chat_box .chat_content").eq(index).remove();
        $(this).parent("li").remove();

        //如果当前删除节点还有下一条，则删除当前后将显示的active移到下一条
        if ($(".user_list li").length > index) {
            $(".user_list li").eq(index).addClass("active");
            $(".chat_box .chat_content").hide();
            $(".chat_box .chat_content").eq(index).show();
        } else {//如果当前删除节点没有下一条，则删除当前后将显示的active移到上一条
            $(".user_list li").eq(index - 1).addClass("active");
            $(".chat_box .chat_content").hide();
            $(".chat_box .chat_content").eq(index - 1).show();
        }
    } else {//非当前active节点，直接清除
        var index = $(this).parent("li").index();
        $(".chat_box .chat_content").eq(index).remove();
        $(this).parent("li").remove();
    }
})

