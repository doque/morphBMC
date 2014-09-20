SELECT r.name,
       r.value,
       p.name as param1,
       a.name as att1,
       p2.name as param2,
       a2.name as att2
FROM compatibility c
JOIN attribute a ON c.att1_id = a.id
JOIN attribute a2 ON c.att2_id = a2.id
JOIN PARAMETER p ON a.parameter_id = p.id
JOIN PARAMETER p2 ON a2.parameter_id = p2.id
JOIN rating r ON c.rating_id = r.id
WHERE p.problem_id = ?
  OR p2.problem_id = ?