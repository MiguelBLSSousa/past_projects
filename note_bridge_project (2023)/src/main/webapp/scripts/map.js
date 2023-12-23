let instruments = [];
let zips = {};
let features = [];

//Icon styles for the map (are added later)
const styles = {};

//Creating the map
const map = new ol.Map({
    target: 'map',
    layers: [
        new ol.layer.Tile({
            source: new ol.source.OSM()
        })
    ],
    view: new ol.View({
        //The center of the Netherlands
        center: ol.proj.fromLonLat([5.42789, 52.05274]),
        zoom: 7
    })
});

function getIcon(instrument) {
    return styles[instrument];
}

//Creating the markers layer for the map
const markers = new ol.layer.Vector({
    source: new ol.source.Vector(),
    style: new ol.style.Style({
        image: new ol.style.Icon({
            anchor: [0.5, 1],
            src: 'img/pin-musicnode.png',
            scale: 0.08
        })
    })
});
map.addLayer(markers);

async function loadData() {
    //get and load instruments
    const instruments_data = await fetch('./json/instruments.json', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
        },
    });
    instruments = await instruments_data.json();

    loadInstruments();

    //get and load teachers
    const teacher_data = await fetch('./api/teachers', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
        },
    });

    // creating zips with teachers inside
    zips = getZips(await teacher_data.json());

    //creating markers
    features = await createFeatures();

    await filterInstruments();
    document.getElementById("loader").style.display = "none";
}

function loadInstruments() {
    let filter = document.getElementById("instuments-filter");
    let result = "";
    for (let i = 0; i < instruments.length; i++) {
        let instrument = instruments[i];
        result += "<li><div class=\"map-filter-icon\" style=\"background: url('" + instrument.url + "'); background-size: contain; background-position: center; background-repeat: no-repeat;\"></div>";
        result += "<p id=\"test\">" + instrument.name.charAt(0).toUpperCase() + instrument.name.slice(1) + "</p>";
        result += "<input name='instruments[]' value='" + instrument.name + "' type=\"checkbox\" onchange=\"filterInstruments('" + instrument.name + "', this.checked)\"></li>";
        styles[instrument.name] = new ol.style.Style({
            image: new ol.style.Icon({
                anchor: [0.5, 1],
                src: instrument.pin,
                scale: 0.10
            })
        })
    }
    filter.innerHTML = result;
}

//filter teachers
async function filterInstruments() {
    markers.getSource().clear();

    const selectedInstruments = [];

    //getting all checked instruments
    let checkboxes = document.querySelectorAll("input[name='instruments[]']:checked");
    checkboxes.forEach(function (checkbox) {
        selectedInstruments.push(checkbox.value);
    });

    // if none instruments are selected show all instruments
    if (selectedInstruments.length === 0) {
        for (const feature of features) {
            markers.getSource().addFeature(feature);
        }
        return
    }

    //filter the instruments
    for (const feature of features) {
        const instruments = getInstrumentsByZip(feature.zip);
        if (selectedInstruments.some(instrument => instruments.includes(instrument))) {
            markers.getSource().addFeature(feature);
        }
    }
}

/**
 * Creates zip dictionary from the teachers json which contains zip addresses. Each zip address contains all the teachers on that zip.
 * @param teachers the teachers json
 * @returns {{}} the zips with corresponding teachers
 */
function getZips(teachers) {
    const zipcodes = {};

    for (let i = 0; i < teachers.length; i++) {
        const teacher = teachers[i];
        const {tid, firstname, surname, instruments, zip, picture} = teacher;

        if (!zipcodes[zip]) {
            zipcodes[zip] = [];
        }

        zipcodes[zip].push({tid, name: firstname + ' ' + surname, instruments, picture});
    }

    return zipcodes;
}

/**
 * Creates features from the zips.
 *
 * @returns {Promise<void>} the features
 */
async function createFeatures() {
    const features = [];
    for (const zipCode in zips) {
        const zip = zips[zipCode];
        if (zip.length === 0) {
            continue;
        }

        //Gets all distinct instruments on the same zip
        const instruments = getInstrumentsByZip(zip);
        if (instruments.length === 0) {
            continue;
        }

        //Get location from zipcode
        const addr = await getAddress(zipCode.toString().trim());
        if (addr.length === 0) {
            continue;
        }

        const coord = [parseFloat(addr[0].lon), parseFloat(addr[0].lat)];
        const marker = new ol.Feature(new ol.geom.Point(ol.proj.fromLonLat(coord)));
        const icon = instruments.length > 1 ? null : getIcon(instruments[0]);
        marker.setStyle(icon);
        marker.zip = zip;
        map.on('click', function (evt) {
            let zip = map.forEachFeatureAtPixel(evt.pixel,
                function (feature) {
                    return feature.zip;
                });
            showTeachers(zip);
        });
        features.push(marker);

    }
    return features;
}

/**
 * Gets all the instruments from the zip
 * @param zip the zip
 * @returns {any[]} all the instruments
 */
function getInstrumentsByZip(zip) {
    const zipInstruments = new Set();
    zip.forEach(t => {
        t.instruments.forEach(i => {
            zipInstruments.add(i);
        });
    });
    return Array.from(zipInstruments);
}

/**
 * Only let the instruments show inside the filter that contain the search value.
 */
function searchInstruments() {
    let search = document.getElementById("search").value;
    let instrumentFilter = document.getElementById("instuments-filter");
    for (let index = 0; index < instrumentFilter.childElementCount; index++) {
        const instrument = instrumentFilter.children[index].getElementsByTagName("p")[0].innerText;

        if (!instrument.toLowerCase().includes(search.toLowerCase())) {
            instrumentFilter.children[index].style.display = "none";
        } else {
            instrumentFilter.children[index].style.display = null;
        }
    }
}

//Postal Code Lookup
let addressPos = null;
const NOMINATIM_BASE_URL = "https://nominatim.openstreetmap.org/search?";
const requestOptions = {
    method: "GET",
    redirect: "follow",
};

async function getAddress(postalcode) {
    const params = {
        country: "Netherlands",
        format: "json",
        postalcode: postalcode,
    };
    const queryString = new URLSearchParams(params).toString();

    let res = await fetch(`${NOMINATIM_BASE_URL}${queryString}`, requestOptions)
        .then((response) => response.json())
        .catch((err) => console.log("err: ", err));
    return res;
}

//Centering map and zooming
async function CenterMap(postal_code) {
    let addr = await getAddress(postal_code);
    if (addr !== []) {
        //coordinates gathered from the postal coded from getAddress() to be zoomed into
        let coord = [parseFloat(addr[0].lon), parseFloat(addr[0].lat)];
        map.getView().animate({center: ol.proj.fromLonLat(coord)}, {zoom: 13})
    }
}

async function homePageSwitch() {
    let searchParams = new URLSearchParams(location.search)
    let adr = searchParams.get("address")
    if (adr !== null) {
        await CenterMap(adr);
    }
}

//Search zipcode filter
window.onload = homePageSwitch();
var scaleMultiplier = 6;

var iconStyle = new ol.style.Icon({
    src: 'img/area.png',
});

var areaStyles = [new ol.style.Style({
    image: iconStyle
})];

var areas = new ol.layer.Vector({
    source: new ol.source.Vector(),
    style: function (feature, resolution) {
        var scale = adjustScale(scaleMultiplier, resolution);
        iconStyle.setScale(scale);
        return areaStyles;
    }
});

function adjustScale(ratio, resolution) {
    let scale = ratio / resolution;
    return scale;
}

map.addLayer(areas);

var pin_locations = new ol.layer.Vector({
    source: new ol.source.Vector(),
    style: new ol.style.Style({
        image: new ol.style.Icon({
            src: 'img/pin-area.png',
            anchor: [0.5, 1],
            scale: 0.08
        })
    })
});
map.addLayer(pin_locations);

async function filterAddress() {
    let search_val = document.getElementById("location_search").value;
    await CenterMap(search_val);

    //set marker
    let addr = await getAddress(search_val);
    let coord = [parseFloat(addr[0].lon), parseFloat(addr[0].lat)];
    pin_locations.getSource().clear();
    areas.getSource().clear();
    pin_location = new ol.Feature(new ol.geom.Point(ol.proj.fromLonLat(coord)));
    pin_locations.getSource().addFeature(pin_location);
    area = new ol.Feature(new ol.geom.Point(ol.proj.fromLonLat(coord)));
    areas.getSource().addFeature(area);
    //to commit
}

function setScaleMultiplier(value){
    scaleMultiplier = value;
    let search_val = document.getElementById("location_search").value;
    if(search_val !== null){
        filterAddress();
    }
}

function createTeacherObject(tid, name, instruments, picture) {
    const teacher = "<div onclick='teacherprofile(%tid)' class=\"teacher\">\n" +
    "    <img src=\".%picture\" class=\"profile-pic\" alt=\"profile-pic\"/>\n" +
    "    <p class=\"name\">%name</p>\n" +
    "    <p class=\"instruments\">%instruments</p>\n" +
    "</div>"

    return teacher.replace("%tid", tid).replace("%name", name).replace("%instruments", instruments).replace("%picture", picture);
}

function showTeachers(zip) {
    const container = document.getElementById("zip-container");
    const closeBtn = document.getElementById("close-btn");

    let t = "";

    for (const teacher of zip) {
        t += createTeacherObject(teacher.tid, teacher.name, teacher.instruments.join(" & "), teacher.picture);
    }
    container.innerHTML = t;
    container.style.display = null;
    closeBtn.style.display = null;
}

function closeZip() {
    const container = document.getElementById("zip-container");
    const closeBtn = document.getElementById("close-btn");
    container.style.display = "none";
    closeBtn.style.display = "none";
}

function teacherprofile(tid) {
    location.href = "./profile.jsp?tid=" + tid;
}

function toggleFilter() {
    const closeBtn = document.getElementById("close-btn-filter");
    const filter = document.getElementById("filter");
    const map = document.getElementById("map");

    if (filter.style.display === "none") {
        filter.style.display = null;
        closeBtn.style.transform = "rotate(0deg)";
    } else {
        filter.style.display = "none";
        closeBtn.style.transform = "rotate(180deg)";
    }
}

function clearAddress() {
    pin_locations.getSource().clear();
    areas.getSource().clear();
}