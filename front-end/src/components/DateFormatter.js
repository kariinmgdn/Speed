export const formatDate = (date) => {
    date = new Date(date);
    const month = formatNumbers(date.getMonth() + 1);
    return `${date.getFullYear()}-${month}-${formatNumbers(date.getDate())} `
        +`${formatNumbers(date.getHours())}:${formatNumbers(date.getMinutes())}:${formatNumbers(date.getSeconds())}`;
}

export const formatNumbers = (number) => {
    return number < 10 ? "0" + number : number;
}