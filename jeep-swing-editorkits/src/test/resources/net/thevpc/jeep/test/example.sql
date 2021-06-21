SELECT First_Name, Nickname FROM Friends WHERE Nickname LIKE '%brain%';

SELECT
	country.country_name_eng,
	SUM(CASE WHEN call.id IS NOT NULL THEN 1 ELSE 0 END) AS calls,
	AVG(ISNULL(DATEDIFF(SECOND, call.start_time, call.end_time),0)) AS avg_difference
FROM country
LEFT JOIN city ON city.country_id = country.id
LEFT JOIN customer ON city.id = customer.city_id
LEFT JOIN call ON call.customer_id = customer.id
GROUP BY
	country.id,
	country.country_name_eng
HAVING AVG(ISNULL(DATEDIFF(SECOND, call.start_time, call.end_time),0)) > (SELECT AVG(DATEDIFF(SECOND, call.start_time, call.end_time)) FROM call)
ORDER BY calls DESC, country.id ASC;
