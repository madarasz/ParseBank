function formatMoney(input) {
    input = input.toString();
    var length = input.length;
    while (length > 3) {
        length -= 3;
        input = input.substring(0, length) + " " + input.substring(length);
    }
    return input + " Ft";
}

