-- Task12.sql
-- Title: Player Matchups – Unique Pair Generation and Variations
--
-- Problem:
--   From a PLAYER table, list all unique, unordered player pairs (no self-pairs,
--   no duplicates like (A,B) and (B,A)). Output should be player1.name, player2.name.
--
-- Notes:
--   - This script includes: instructions, schema, sample data, primary solution,
--     alternative solutions, persistence examples, and performance notes.
--   - All sections are commented; you can run the DDL/DML, then any SELECTs you want.
--
-- ----------------------------------------------------------------------------
-- 1) Schema (DDL)
-- ----------------------------------------------------------------------------

-- Drop if exists (for reruns; may differ by RDBMS)
-- PostgreSQL:
-- DROP TABLE IF EXISTS match_unordered;
-- DROP TABLE IF EXISTS match_directed;
-- DROP TABLE IF EXISTS player;

-- MySQL:
-- DROP TABLE IF EXISTS match_unordered;
-- DROP TABLE IF EXISTS match_directed;
-- DROP TABLE IF EXISTS player;

CREATE TABLE player (
  id   INT PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);

-- Optional: a grouping column if you want group/league-limited pairings
-- ALTER TABLE player ADD COLUMN group_id INT;

-- ----------------------------------------------------------------------------
-- 2) Sample Data (DML)
-- ----------------------------------------------------------------------------

INSERT INTO player (id, name) VALUES
  (1, 'John'),
  (2, 'Maya'),
  (3, 'Bill'),
  (4, 'Casy');

-- Example groups (uncomment if you added group_id)
-- UPDATE player SET group_id = 1 WHERE id IN (1,2);
-- UPDATE player SET group_id = 2 WHERE id IN (3,4);

-- ----------------------------------------------------------------------------
-- 3) Primary Solution (Best Practice)
-- ----------------------------------------------------------------------------
-- Requirement:
--   Unique unordered pairs of players (no self-pairs, no duplicates).
-- Rationale:
--   p1.id < p2.id enforces strict ordering, avoiding both self-pairs and duplicates.
--   Efficient on indexed primary key (id).

-- Output: player1.name, player2.name
SELECT p1.name AS player1, p2.name AS player2
FROM player p1
JOIN player p2
  ON p1.id < p2.id
ORDER BY p1.id, p2.id;

-- Expected output with sample data:
-- player1 | player2
-- --------+--------
-- John    | Maya
-- John    | Bill
-- John    | Casy
-- Maya    | Bill
-- Maya    | Casy
-- Bill    | Casy

-- ----------------------------------------------------------------------------
-- 4) Variations / Alternative Approaches
-- ----------------------------------------------------------------------------

-- 4.a) Include IDs (useful for joins or persistence)
SELECT p1.id AS id1, p1.name AS name1,
       p2.id AS id2, p2.name AS name2
FROM player p1
JOIN player p2
  ON p1.id < p2.id
ORDER BY id1, id2;

-- 4.b) Directed pairs (home/away) – both (A,B) and (B,A), no self-pairs
SELECT p1.name AS home, p2.name AS away
FROM player p1
JOIN player p2
  ON p1.id <> p2.id
ORDER BY home, away;

-- 4.c) Uniqueness by NAME when you cannot trust numeric order or IDs (heavier)
--     Uses LEAST/GREATEST + DISTINCT to canonicalize pair ordering on the fly.
SELECT DISTINCT
       LEAST(p1.name, p2.name)   AS player1,
       GREATEST(p1.name, p2.name) AS player2
FROM player p1
JOIN player p2
  ON p1.name <> p2.name
ORDER BY player1, player2;

-- 4.d) Pair only within a group/league (requires group_id column)
-- SELECT p1.group_id,
--        p1.name AS player1, p2.name AS player2
-- FROM player p1
-- JOIN player p2
--   ON p1.group_id = p2.group_id
--  AND p1.id < p2.id
-- ORDER BY p1.group_id, p1.id, p2.id;

-- 4.e) Window-function variant (engines with analytic functions)
--     Row-number the players deterministically, then pair on rn
WITH numbered AS (
  SELECT p.*, ROW_NUMBER() OVER (ORDER BY id) AS rn
  FROM player p
)
SELECT a.name AS player1, b.name AS player2
FROM numbered a
JOIN numbered b
  ON a.rn < b.rn
ORDER BY a.id, b.id;

-- ----------------------------------------------------------------------------
-- 5) Persisting Matches
-- ----------------------------------------------------------------------------

-- 5.a) Unordered unique matches (id1 < id2 enforced)
CREATE TABLE match_unordered (
  id1 INT NOT NULL,
  id2 INT NOT NULL,
  CONSTRAINT pk_match_unordered PRIMARY KEY (id1, id2),
  CONSTRAINT chk_order CHECK (id1 < id2)
  -- ,FOREIGN KEY (id1) REFERENCES player(id)
  -- ,FOREIGN KEY (id2) REFERENCES player(id)
);

-- Populate
INSERT INTO match_unordered (id1, id2)
SELECT p1.id, p2.id
FROM player p1
JOIN player p2
  ON p1.id < p2.id;

-- View persisted pairs
SELECT * FROM match_unordered ORDER BY id1, id2;

-- 5.b) Directed matches (home/away), both directions allowed
CREATE TABLE match_directed (
  home_id INT NOT NULL,
  away_id INT NOT NULL,
  CONSTRAINT pk_match_directed PRIMARY KEY (home_id, away_id),
  CONSTRAINT chk_neq CHECK (home_id <> away_id)
  -- ,FOREIGN KEY (home_id) REFERENCES player(id)
  -- ,FOREIGN KEY (away_id) REFERENCES player(id)
);

-- Populate
INSERT INTO match_directed (home_id, away_id)
SELECT p1.id, p2.id
FROM player p1
JOIN player p2
  ON p1.id <> p2.id;

-- View persisted directed pairs
SELECT * FROM match_directed ORDER BY home_id, away_id;

-- ----------------------------------------------------------------------------
-- 6) Counts & Sanity Checks
-- ----------------------------------------------------------------------------

-- 6.a) Count total players and total unique unordered pairs (n choose 2)
WITH n AS (SELECT COUNT(*) AS cnt FROM player)
SELECT cnt AS total_players,
       (cnt * (cnt - 1)) / 2 AS total_unordered_pairs
FROM n;

-- 6.b) Count pairs produced by the primary query
SELECT COUNT(*) AS produced_pairs
FROM player p1
JOIN player p2 ON p1.id < p2.id;

-- ----------------------------------------------------------------------------
-- 7) Performance Notes
-- ----------------------------------------------------------------------------
-- - Ensure PLAYER(ID) is a PRIMARY KEY (index-backed).
-- - For group-limited pairing, a composite index (group_id, id) helps.
-- - Prefer p1.id < p2.id over p1.id <> p2.id + DISTINCT:
--     * avoids generating duplicates and then deduping
--     * leverages PK ordering efficiently
-- - LEAST/GREATEST + DISTINCT is portable but heavier; use only when necessary.
--
-- End of Task12.sql
