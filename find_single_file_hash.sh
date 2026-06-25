#!/bin/bash

# 参数说明：$1=搜索目录  $2=要查找的文件名
if [ $# -ne 2 ]; then
    echo "用法：$0 搜索目录 目标文件名"
    echo "示例：$0 ./test app.zip"
    exit 1
fi

SEARCH_DIR="$1"
TARGET_FILE="$2"

# 校验目录存在
if [ ! -d "$SEARCH_DIR" ]; then
    echo "错误：目录 [$SEARCH_DIR] 不存在"
    exit 1
fi

echo "正在递归查找文件：$TARGET_FILE ，目录：$SEARCH_DIR"
echo "----------------------------------------"

# find 精准匹配文件名，只找普通文件
find "$SEARCH_DIR" -type f -name "$TARGET_FILE" | while read -r filepath; do
    echo "找到文件：$filepath"

    # 计算哈希
    md5_val=$(md5sum "$filepath" 2>/dev/null | awk '{print $1}')
    sha256_val=$(sha256sum "$filepath" 2>/dev/null | awk '{print $1}')

    echo "MD5:    $md5_val"
    echo "SHA256: $sha256_val"
    echo "----------------------------------------"
done

echo "查找完成"