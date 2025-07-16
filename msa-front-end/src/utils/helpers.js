export function translateForByte(bytes) {
  bytes = Number(bytes) < 0 ? 0 : bytes;
  return parseFloat((bytes / 1024 / 1024).toFixed(2));
}

export function truncatedForPercent(percent) {
  return Math.floor(percent * 1000) / 10;
}


export function getLast10SecondsTimestamps() {
  const timestamps = [];
  const now = new Date();
  for (let i = 0; i < 10; i++) {
    const pastTime = new Date(now.getTime() - i * 1000);
    const timeStr = pastTime.toTimeString().split(' ')[0];
    timestamps.unshift(timeStr);
  }
  return timestamps;
}