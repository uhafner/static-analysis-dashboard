$(document).ready(
    function () {
        Chart.defaults.global.elements.rectangle.backgroundColor = '#f7f1da';
        Chart.defaults.global.elements.rectangle.borderColor = '#355564';
        Chart.defaults.global.elements.rectangle.borderWidth = 1;

        $.get("ajax/categories", {origin: "id-is-not-available-yet", reference: "-"},
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
        $.get("ajax/types", {origin: "id-is-not-available-yet", reference: "-"},
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

