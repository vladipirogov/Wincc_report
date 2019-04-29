function loadreports () {

    this.source = null;

    this.start = function () {

        var reportTable = document.getElementById("reports_table");

        this.source = new EventSource("/report/stream");

        this.source.addEventListener("message", function (event) {

            // These events are JSON, so parsing and DOM fiddling are needed
            var report = JSON.parse(event.data);
            var rowObject = rowDto(report);

            //$("#reports-body").empty();
            var row = reportTable.getElementsByTagName("tbody")[0].insertRow(-1);

            Object.keys(rowObject).forEach(function(key, index) {
                var cell = row.insertCell(index);
                cell.innerHTML = rowObject[key];
            });
        });

        this.source.onerror = function() {
            this.close();
        };

    };

    this.stop = function() {
        this.source.close();
    }

}

function rowDto(report) {
   var result = {
       dateTime:report.dateTime.replace(" ", "&nbsp;&nbsp;"),
       num:report.num,
       rname:report.rname,
       rub05:report.rub05,
       rub510:report.rub510,
       rub1020:report.rub1020,
       rub2040:report.rub2040,
       cellulose:report.cellulose,
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

var flux = new loadreports();

/*
 * Register callbacks for starting and stopping the SSE controller.
 */
window.onload = function() {
    flux.start();
};
window.onbeforeunload = function() {
    flux.stop();
    return '';
}