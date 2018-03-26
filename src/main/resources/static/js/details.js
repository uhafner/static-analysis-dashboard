$(document).ready(
    function () {
        $.get("priorities", {id: "hello-world"},
            function (data) {
                console.log(data);

                var summary = document.getElementById("priority-chart");
                var context = summary.getContext("2d");
                context.height = 300;
                context.width = 300;
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
        $.get("categories", {id: "hello-world"},
            function (data) {
                console.log(data);

                var summary = document.getElementById("categories-chart");
                var context = summary.getContext("2d");
                context.height = 300;
                context.width = 300;
                var prioritiesSummaryChart = new Chart(context, {
                    type: 'horizontalBar',
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

