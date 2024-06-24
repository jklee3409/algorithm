# 방법 1

class IncrementalHeap:
    def __init__(self):
        self.heap = [0]  # 더미 데이터로 시작
        self.moves = 0  # 이동 횟수를 저장할 변수

    def insert(self, value):
        self.heap.append(value)
        self._sift_up(len(self.heap) - 1)

    def _sift_up(self, idx):
        parent = idx // 2
        if idx > 1 and self.heap[idx] < self.heap[parent]:
            self.heap[idx], self.heap[parent] = self.heap[parent], self.heap[idx]
            self.moves += 1  # 데이터 이동이 발생했으므로 증가
            self._sift_up(parent)

    def get_sorted_elements(self):
        sorted_elements = []
        while len(self.heap) > 1:
            sorted_elements.append(self.extract_min())
        return sorted_elements

    def extract_min(self):
        if len(self.heap) == 2:
            return self.heap.pop()
        root = self.heap[1]
        self.heap[1] = self.heap.pop()
        self._sift_down(1)
        return root

    def _sift_down(self, idx):
        smallest = idx
        left = 2 * idx
        right = 2 * idx + 1
        n = len(self.heap)

        if left < n and self.heap[left] < self.heap[smallest]:
            smallest = left
        if right < n and self.heap[right] < self.heap[smallest]:
            smallest = right
        if smallest != idx:
            self.heap[idx], self.heap[smallest] = self.heap[smallest], self.heap[idx]
            self.moves += 1  # 데이터 이동이 발생했으므로 증가
            self._sift_down(smallest)

# 예제 사용
data = [0, 5, 9, 2, 17, 6, 13, 11, 7, 15]
incremental_heap = IncrementalHeap()

for item in data[1:]:
    incremental_heap.insert(item)

sorted_data = incremental_heap.get_sorted_elements()
print(sorted_data)  # 정렬된 리스트 출력
print(f"방법 1의 데이터 이동 횟수: {incremental_heap.moves}")

print()

# 방법 2

def heapify(arr, n, i, moves):
    largest = i  # 루트로 초기화
    left = 2 * i  # 왼쪽 자식
    right = 2 * i + 1  # 오른쪽 자식

    # 왼쪽 자식이 루트보다 크다면
    if left < n and arr[i] < arr[left]:
        largest = left

    # 오른쪽 자식이 현재 가장 큰 것보다 크다면
    if right < n and arr[largest] < arr[right]:
        largest = right

    # 가장 큰 것이 루트가 아니라면
    if largest != i:
        arr[i], arr[largest] = arr[largest], arr[i]  # 교체
        moves[0] += 1  # 데이터 이동이 발생했으므로 증가

        # 교체 후에 힙을 재귀적으로 힙화
        heapify(arr, n, largest, moves)

def heap_sort(arr):
    moves = [0]  # 데이터 이동 횟수를 저장할 리스트
    n = len(arr)

    # 최대 힙을 구축
    for i in range(n//2, 0, -1):
        heapify(arr, n, i, moves)

    # 하나씩 요소를 추출해서 정렬
    for i in range(n-1, 0, -1):
        arr[i], arr[1] = arr[1], arr[i]  # 현재 루트와 교체
        moves[0] += 1  # 데이터 이동이 발생했으므로 증가
        heapify(arr, i, 1, moves)

    return arr, moves[0]

# 예제 사용
data = [0, 5, 9, 2, 17, 6, 13, 11, 7, 15]
sorted_data, move_count = heap_sort(data[:])
print(sorted_data[1:])  # 정렬된 리스트 출력 (더미 데이터 제외)
print(f"방법 2의 데이터 이동 횟수: {move_count}")
