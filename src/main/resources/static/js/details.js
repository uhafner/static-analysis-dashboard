$(document).ready(
    function () {
        $.get("data", {id: "hello-world"},
            function (data) {
                console.log(data);

                var summary = document.getElementById("details-chart");
                var context = summary.getContext("2d");
                context.height = 200;
                context.width = 200;
                var prioritiesSummaryChart = new Chart(context, {
                    type: 'doughnut',
                    data: {
                        labels: ["High", "Normal", "Low"],
                        datasets: [{
                            label: 'High priority, Normal priority, Low priority',
                            data: data,
                            backgroundColor: [
                                '#f5c6cb',
                                '#ffeeba',
                                '#b8daff'
                            ],
                            hoverBackgroundColor: [
                                '#f5929f',
                                '#ffeb75',
                                '#53bdff'
                            ],
                            hoverBorderColor: [
                                '#fff', '#fff', '#fff'
                            ]
                        }]
                    }
                });
            })
    });

