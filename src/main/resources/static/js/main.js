var mainTable = null;


fillTable = (data) => {
    var rowObject = rowDto(data);
    var result = [];

    Object.keys(rowObject).forEach(function(key, index) {
        result.push(rowObject[key]);
    });
    return result;
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
        .catch(error => console.log(error))
}

function autoUpdate () {
    var timer = null;
    var timeout = 1;
    var tableDataSet = null;

    dataSet : tableDataSet;

    fillRows = (data) => {
        data.forEach(function (element) {
            tableDataSet.push(fillTable(element)) // Prints result from `response.json()`
        })
        mainTable.rows.add(tableDataSet).draw();
    }

    callbackTime = () => {
        tableDataSet = [];
        $('#reports_table').dataTable().fnFilter('');
        $('#reports_table').dataTable().fnSort([ 0, "desc" ]);
        var table = document.getElementById("reports_table");
        var rows = table.getElementsByTagName("tbody")[0].rows;
        var cells = rows[0] != null ? rows[0].cells : null;
        var dateInput = null;
        if(cells != null && cells[0].innerHTML != 'В таблице отсутствуют данные') {
            var cellValue = cells[0].innerHTML.replace('&nbsp;', '').replace('&nbsp;', ' ');
            dateInput = moment(cellValue, 'YYYY-MM-DD HH:mm:ss').add(1, 'seconds').utc(-120).format();
        }
        else {
            var nextDate = new Date();
            nextDate.setHours(0, 0, 0, 0);
            dateInput = moment(nextDate, 'YYYY-MM-DD HH:mm:ss').utc(-120).format();
        }

        var currentDate = new Date();
        currentDate.setHours(0,0,0,0);
        var endDateInput = moment(currentDate, 'YYYY-MM-DD HH:mm:ss').add(1, 'days').utc(-120).format();
        var value = {dateInput : dateInput, endDateInput:endDateInput};

        request('/find_updates', value, 'POST', fillRows)
    }

    this.findUpdates = () => {
        if(timer != null) {
            showMessageBox('Таймер уже запущен!');
            return;
        }

        $("#auto-start-btn").addClass( "color-start" );
        if(mainTable != null) {
                mainTable.clear();
                mainTable.draw();
            }

        request('/setting', '', 'POST', function (data) {
            timeout = data.updateInterval;
            timer = setInterval(callbackTime, timeout*1000);
        })

    }

    this.stopUpdates = () => {
        $("#auto-start-btn").removeClass( "color-start" );
        clearInterval(timer);
        timer = null;
    }

    this.findByPeriod = () => {
        if(updates != null)
            this.stopUpdates();
        mainTable.clear();
        mainTable.draw();
        tableDataSet = [];
        var input = document.getElementById('report-date-input').value
        var dateInput = moment(input, 'YYYY-MM-DD HH:mm:ss').utc(-120).format();
        var endDateInput = moment(input, 'YYYY-MM-DD HH:mm:ss').add(1, 'days').utc(-120).format();
        var value = {dateInput : dateInput, endDateInput:endDateInput};
        request('/find_updates', value, 'POST', fillRows)
    }
}


var updates = new autoUpdate();
updates.findUpdates();

$(document).ready(function() {
  mainTable = $('#reports_table').DataTable({
         "pageLength": 25,
         "order": [[ 0, "desc" ]],
         language: {
                 processing:     "Подождите...",
                 search:         "Поиск:",
                 lengthMenu:    "Показать _MENU_ записей",
                 info:           "Записи с _START_ до _END_ из _TOTAL_ записей",
                 infoEmpty:      "Записи с 0 до 0 из 0 записей",
                 infoFiltered:   "(отфильтровано из _MAX_ записей)",
                 infoPostFix:    "",
                 loadingRecords: "Загрузка записей...",
                 zeroRecords:    "Записи отсутствуют.",
                 emptyTable:     "В таблице отсутствуют данные",
                 paginate: {
                     first:      "Первая",
                     previous:   "Предыдущая",
                     next:       "Следующая",
                     last:       "Последняя"
                 },
                 aria: {
                     sortAscending:  ": активировать для сортировки столбца по возрастанию",
                     sortDescending: ": активировать для сортировки столбца по убыванию"
                 }
             }
    });
    $("#reports_table").removeClass( "dataTable" );
} );