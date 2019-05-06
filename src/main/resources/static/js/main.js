
fillTable = (data) => {
    var rowObject = rowDto(data);
    var reportTable = document.getElementById("reports_table");
    var row = reportTable.getElementsByTagName("tbody")[0].insertRow(-1);

    Object.keys(rowObject).forEach(function(key, index) {
        var cell = row.insertCell(index);
        cell.style.width = '5.88%';
        cell.innerHTML = rowObject[key];
    });
}

function rowDto(report) {
   var result = {
       dateTime:report.dateTime.replace(" ", "&nbsp;&nbsp;"),
       num:report.num,
       code:report.code,
       rname:report.rname,
       rub05:report.rub05,
       rub510:report.rub510,
       rub1020:report.rub1020,
       rub2040:report.rub2040,
       cellulose:report.cellulose/10,
       mineral:report.mineral,
       dust:report.dust,
       bitum:report.bitum,
       temper:report.temper,
       mixTime:report.mixTime,
       weight:report.weight,
       allWeight:report.allWeight,
       oper:report.oper
   };
   return result;
}

request = (url, value, method, fun) => {
    var payload = { method: method,
        mode: 'cors',
        cache: 'default',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'},
        body:JSON.stringify(value)
    };
    fetch(url, payload)
        .then(response => response.json()) // Result from the
        .then(data => {
           fun(data);
        })
        .catch(error => console.error(error))
}

function autoUpdate () {
    var timer = null;
    var timeout = 1;

    fillRows = (data) => {
        data.forEach(function (element) {
            fillTable(element) // Prints result from `response.json()`
        })
    }

    callbackTime = () => {
        var table = document.getElementById("reports_table");
        var rows = table.getElementsByTagName("tbody")[0].rows;
        var cells = rows[rows.length-1] != null ? rows[rows.length-1].cells : null;
        var dateInput = null;
        if(cells != null) {
            var cellValue = $.date(cells[0].innerHTML.replace(/&nbsp;/gi, ''),'format', 'Y-m-d H:i:s');
            var date = new Date(moment(cellValue, "YYYY.MM.DDHH:mm:ss"));
            date.setSeconds(date.getSeconds() + 1)
            dateInput = $.date(date, 'format', 'Y-m-d H:i:s')
        }
        else {
            dateInput = $.date(new Date().setHours(0, 0, 0, 0), 'format', 'Y-m-d H:i:s');
        }

        var currentDate = new Date();
        currentDate.setHours(0,0,0,0);
        var endDateInput = $.date(currentDate.setDate(currentDate.getDate() + 1), 'format', 'Y-m-d H:i:s');
        var value = {dateInput : dateInput, endDateInput:endDateInput};
        //console.log("value = " + value);

        request('/find_updates', value, 'POST', fillRows)
    }

    this.findUpdates = () => {
        if(timer != null) {
            showMessageBox('Таймер уже запущен!');
            return;
        }

        $("#reports-body").empty();

        request('/setting', '', 'POST', function (data) {
            timeout = data.updateInterval;
            timer = setInterval(callbackTime, timeout*1000);
        })

    }

    this.stopUpdates = () => {
        clearInterval(timer);
        timer = null;
    }
}


findByPeriod = () => {
    if(updates != null)
        updates.stopUpdates();
    $("#reports-body").empty();
    var input = document.getElementById('report-date-input').value
    var date = new Date(input);
    var dateInput = $.date(input, 'format', 'Y-m-d H:i:s');
    var endDateInput = $.date(date.setDate(date.getDate() + 1), 'format', 'Y-m-d H:i:s');
    var value = {dateInput : dateInput, endDateInput:endDateInput};
    request('/find_updates', value, 'POST', fillRows)
}

var updates = new autoUpdate();
updates.findUpdates();