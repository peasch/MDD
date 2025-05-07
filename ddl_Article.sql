CREATE TABLE mdd.article
(
    id        INT AUTO_INCREMENT NOT NULL,
    theme_id  INT                NULL,
    content   VARCHAR(2000)      NULL,
    author_id INT                NULL,
    CONSTRAINT pk_article PRIMARY KEY (id)
);

ALTER TABLE mdd.article
    ADD CONSTRAINT FK_ARTICLE_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES mdd.user (id);