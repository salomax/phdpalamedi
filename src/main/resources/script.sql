ALTER TABLE tb_article_content ADD FULLTEXT INDEX (content);
ALTER TABLE tb_article ADD FULLTEXT INDEX (title, author, summary, keywords);
ALTER TABLE tb_article ADD FULLTEXT(title);
ALTER TABLE tb_article ADD FULLTEXT(summary);
ALTER TABLE tb_article ADD FULLTEXT(author);
ALTER TABLE tb_article ADD FULLTEXT(keywords);






SELECT p.name, COUNT(a.id) as 'count'

FROM tb_article a

  INNER JOIN tb_article_content ac
      ON a.id = ac.article_id

  INNER JOIN tb_publication pub
      ON pub.id = a.publication_id

  INNER JOIN tb_publisher p
      ON p.id = pub.publisher_id

WHERE status = 0 AND (

	/* filtro */

)

GROUP BY p.name
ORDER BY count DESC






DELETE
FROM tb_article_content
WHERE article_id IN (

	SELECT a.id
	FROM tb_article a
		INNER JOIN tb_publication pub ON a.publication_id = pub.id
	WHERE pub.publisher_id = 17

)

DELETE
FROM tb_article
WHERE publication_id IN (

	SELECT id
	FROM tb_publication
	WHERE publisher_id = 17

)

DELETE
FROM tb_publication
WHERE publisher_id = 17


DELETE
FROM tb_publisher
WHERE id = 17

