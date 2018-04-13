$(document).ready(
    function () {
        Chart.defaults.global.elements.rectangle.backgroundColor = '#f7f1da';
        Chart.defaults.global.elements.rectangle.borderColor = '#355564';
        Chart.defaults.global.elements.rectangle.borderWidth = 1;

        var origin = $('#origin').text();
        var reference = $('#reference').text();
        $.get("ajax/categories", {origin: origin, reference: reference},
            function (categories) {
                new Chart($("#categories-chart"), {
                    type: 'horizontalBar',
                    label: 'Categories',
                    data: categories,
                    options: {
                        legend: {
                            display: false
                        }
                    }
                });
            });
        $.get("ajax/types", {origin: origin, reference: reference},
            function (types) {
                new Chart($("#types-chart"), {
                    type: 'horizontalBar',
                    label: 'Types',
                    data: types,
                    options: {
                        legend: {
                            display: false
                        }
                    }
                });
            });
    });

