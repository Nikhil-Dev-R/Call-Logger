<?php
require_once "../config/database.php";

$stmt = $pdo->query("SELECT * FROM calls ORDER BY created_at DESC");
$calls = $stmt->fetchAll();
?>
<!DOCTYPE html>
<html>
<head>
    <title>Call Logs</title>
    <style>
        body { font-family: sans-serif; padding: 20px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ccc; padding: 8px; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <h1>📞 Call Logs</h1>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Date</th>
                <th>Time</th>
                <th>Type</th>
                <th>Device Number</th>
                <th>Client Number</th>
                <th>Ring Duration (s)</th>
                <th>Call Duration (s)</th>
                <th>Created At</th>
            </tr>
        </thead>
        <tbody>
            <?php foreach ($calls as $call): ?>
                <tr>
                    <td><?= $call['id'] ?></td>
                    <td><?= $call['date'] ?></td>
                    <td><?= $call['time'] ?></td>
                    <td><?= $call['type'] ?></td>
                    <td><?= $call['device_number'] ?></td>
                    <td><?= $call['client_number'] ?></td>
                    <td><?= $call['ring_duration'] ?></td>
                    <td><?= $call['call_duration'] ?></td>
                    <td><?= $call['created_at'] ?></td>
                </tr>
            <?php endforeach ?>
        </tbody>
    </table>
</body>
</html>
