export function formatNumberWithHyphen(value: number | string | null): string {
  if (value === null || value === undefined) {
    return '';
  }
  const valueStr = value.toString();
  if (valueStr.length > 1) {
    const lastDigit = valueStr.slice(-1);
    const restOfNumber = valueStr.slice(0, -1);
    return `${restOfNumber}-${lastDigit}`;
  }
  return valueStr;
}

export const formattedCurrency = (value: number) => {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  }).format(value);
}
