select *
from tb_publisher p
  inner join tb_publication pub on p.id =pub.publisher_id
  inner join tb_article a on pub.id = a.publication_id
  inner join tb_article_content ac on a.id = ac.article_id
where p.id = 38
