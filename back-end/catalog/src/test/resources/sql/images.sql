INSERT INTO images (code, bytes) VALUES 
(RAWTOHEX(HASH('SHA256', STRINGTOUTF8('a'))), STRINGTOUTF8('a')),
(RAWTOHEX(HASH('SHA256', STRINGTOUTF8('b'))), STRINGTOUTF8('b'));