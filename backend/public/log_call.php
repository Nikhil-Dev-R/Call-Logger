<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");
header("Access-Control-Allow-Methods: POST");

require_once "../config/database.php";

// Get raw POST body
$raw = file_get_contents("php://input");
$data = json_decode($raw, true);

// Validate
$required = ["date", "time", "type", "device_number", "client_number", "ring_duration", "call_duration"];
foreach ($required as $key) {
    if (!isset($data[$key])) {
        http_response_code(400);
        echo json_encode(["status" => "error", "message" => "Missing field: $key"]);
        exit;
    }
}

// Insert
try {
    $stmt = $pdo->prepare("
        INSERT INTO calls (date, time, type, device_number, client_number, ring_duration, call_duration)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    ");

    $stmt->execute([
        $data['date'],
        $data['time'],
        $data['type'],
        $data['device_number'],
        $data['client_number'],
        $data['ring_duration'],
        $data['call_duration']
    ]);

    echo json_encode(["status" => "success", "message" => "Call log stored."]);
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(["status" => "error", "message" => "DB Error"]);
}
