-- :name create-domain! :! :n
-- :doc creates a new domain record
INSERT INTO domain
(domain) VALUES (:domain);

-- :name update-domain! :! :n
-- :doc update an existing domain record
UPDATE domain
SET domain = :domain
WHERE id = :id;

-- :name delete-domain! :! :n
-- :doc delete a domain given the id
DELETE FROM domain
WHERE id = :id;

-- :name get-domain :? :1
-- :doc retrieve a domain given the id.
SELECT * FROM domain
WHERE id = :id;

-- :name find-domain-by-name :? :1
-- :doc fetch a domain by domain name.
SELECT * FROM domain
WHERE domain = :domain;

-- :name get-all-domains :? :*
-- :doc fetch all domains
SELECT * FROM domain;

-- ===== --

-- :name create-forwarding! :! :n
-- :doc creates a new forwardings record
INSERT INTO forwardings
(source,destination) VALUES (:source,:destination);

-- :name update-forwarding! :! :n
-- :doc update an existing forwardings record
UPDATE forwardings
SET source = :source, destination = :destination
WHERE id = :id;

-- :name delete-forwarding! :! :n
-- :doc delete a forwarding given the id
DELETE FROM forwardings
WHERE id = :id;

-- :name get-forwarding :? :1
-- :doc retrieve a forwarding given the id.
SELECT * FROM forwardings
WHERE id = :id;

-- :name find-forwarding-by-source :? :1
-- :doc fetch a forwarding by forwarding name.
SELECT * FROM forwardings
WHERE source = :source;

-- :name get-all-forwardings :? :*
-- :doc fetch all forwardings
SELECT * FROM forwardings;

-- ===== --

-- :name create-user! :! :n
-- :doc creates a new users record
INSERT INTO users
(active,username,email,name,password,uid,gid,home,maildir,allowemail)
VALUES (:active,:username,:email,:name,:password,:uid,:gid,:home,:maildir,:allowemail);

-- :name update-user! :! :n
-- :doc update an existing users record
UPDATE users
SET active = :active,
  username = :username,
  email = :email,
  name = :name,
  password = :password,
  uid = :uid,
  gid = :gid,
  home = :home,
  maildir = :maildir,
  allowemail = :allowemail
WHERE id = :id;

-- :name delete-user! :! :n
-- :doc delete a user given the id
DELETE FROM users
WHERE id = :id;


-- :name get-user :? :1
-- :doc retrieve a users given the id.
SELECT * FROM users
WHERE id = :id;

-- :name find-users-by-username :? :1
-- :doc fetch a user by users name.
SELECT * FROM users
WHERE username = :username;

-- :name get-all-users :? :*
-- :doc fetch all users
SELECT * FROM users;

-- ===== --
