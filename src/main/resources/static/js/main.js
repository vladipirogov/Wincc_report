function loadreports () {

    this.source = null;

    this.start = function () {

        var reportTable = document.getElementById("reports");

        this.source = new EventSource("/report/stream");

        this.source.addEventListener("message", function (event) {

            // These events are JSON, so parsing and DOM fiddling are needed
            var report = JSON.parse(event.data);

            //$("#reports-body").empty();
            var row = reportTable.getElementsByTagName("tbody")[0].insertRow(-1);

            Object.keys(report).forEach(function(key, index) {
                var cell = row.insertCell(index);
                //cell.className = "text";
                cell.innerHTML = report[key];
            });
        });

        this.source.onerror = function () {
            this.close();
        };

    };

    this.stop = function() {
        this.source.close();
    }

}

report = new loadreports();

/*
 * Register callbacks for starting and stopping the SSE controller.
 */
window.onload = function() {
    report.start();
};
window.onbeforeunload = function() {
    report.stop();
    return '';
}