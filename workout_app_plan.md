# Workout & Nutrition Tracker — Project Plan

Android-native personal fitness app. Bulking-focused, programme-adherence tracker with calorie byproduct, AI food parser, Health Connect sync, and home-screen widget.

---

## 1. Tech Stack

- **Language & UI:** Kotlin 2.x, Jetpack Compose + Material 3
- **Backend:** Firebase — Auth (Google Sign-In), Firestore (offline persistence on), App Check (Play Integrity)
- **DI:** Hilt
- **Async:** Coroutines + Flow
- **Navigation:** Navigation Compose
- **Serialization:** kotlinx.serialization
- **Charts:** Vico (Compose-native)
- **Widget:** Jetpack Glance
- **AI:** OpenRouter → Gemma 2 9B / Llama 3.1 8B (JSON mode)
- **Health data:** Jetpack Health Connect client
- **Build:** Gradle KTS, version catalog

---

## 2. Fixed Decisions

| Decision | Choice |
|---|---|
| Platform | Android only |
| Auth scope | Me + friends (Firebase Auth w/ Google) |
| MVP feature | Workout + progression tracking |
| Logging style | Post-session entry (live timer later) |
| Detail level | Set-level (sets × reps × weight) |
| Exercise library | ~100 exercises, bundled JSON |
| Bodyweight | Logged (history of values, latest feeds calorie calc) |
| Heavy-day multiplier | Manual toggle per workout |
| Session type | Inferred from routine name (no separate picker) |

---

## 3. Data Model (Firestore)

```
users/{uid}
  displayName, email
  height_cm: Int
  settings: {
    units: "metric" | "imperial"
    weekly_volume_goal_kg: Int
    kcal_target: Int
    protein_target_g: Int
  }

users/{uid}/bodyweight_log/{dateId}       // dateId = YYYY-MM-DD
  date: Timestamp
  weight_kg: Float

users/{uid}/routines/{routineId}
  name: String                             // "Chest & Triceps"
  day_of_week: "MON"…"SUN" | null
  exercises: [
    { exercise_id, target_sets, target_reps, target_weight_kg? }
  ]

users/{uid}/workouts/{workoutId}
  date: Timestamp
  routine_id: String?                      // FK, nullable for ad-hoc workouts
  routine_name: String                     // denormalised for history display
  duration_minutes: Int
  heavy_multiplier: Float                  // 1.0 default, 1.3 if heavy toggled
  notes: String
  calories_burned: Int                     // computed on save
  total_volume_kg: Float                   // computed on save
  exercises: [
    {
      exercise_id: String,
      order: Int,
      sets: [
        { reps: Int, weight_kg: Float, rpe: Int?, notes: String? }
      ]
    }
  ]

users/{uid}/prs/{exerciseId}
  max_weight_kg, max_weight_date, max_weight_workout_id
  estimated_1rm_kg, estimated_1rm_date, estimated_1rm_workout_id
  max_volume_session_kg, max_volume_date, max_volume_workout_id
```

**Phase 2 additions (food parser):**
```
users/{uid}/meals/{mealId}
  date: Timestamp
  raw_input: String                        // "had two eggs and a bowl of rice"
  items: [ { name, quantity, calories, protein_g, carbs_g, fats_g } ]
  total: { calories, protein_g, carbs_g, fats_g }
  source: "ai" | "manual" | "edited"
```

---

## 4. Exercise Library

Bundle as `res/raw/exercises.json` (~100 entries), loaded into singleton `ExerciseLibrary` at app start. MET values sourced from the Compendium of Physical Activities (Ainsworth et al., 2024 edition).

Schema:
```json
{
  "id": "barbell_bench_press",
  "name": "Barbell Bench Press",
  "category": "push",
  "muscle_groups": ["chest", "triceps", "front_delts"],
  "equipment": "barbell",
  "met_value": 5.0,
  "is_compound": true,
  "aliases": ["bench", "bench press", "flat bench"]
}
```

**Categories:** `push`, `pull`, `legs`, `core`, `cardio`, `mobility`
**Coverage target:** 25 push, 25 pull, 25 legs, 10 core, 10 cardio, 5 mobility

---

## 5. Core Algorithms

### 5.1 Calories burned (per workout)

```
kcal = MET × bodyweight_kg × (duration_min / 60) × heavy_multiplier
```

- `MET`: weighted average of MET values of exercises done (weighted by set count)
- `bodyweight_kg`: latest entry from `bodyweight_log`, fallback to user profile
- `heavy_multiplier`: 1.0 default, 1.3 when heavy toggle on
- Runs server-free — pure Kotlin on client, written to `calories_burned` on save

### 5.2 Estimated 1RM (Epley formula)

```
est_1rm = weight × (1 + reps / 30)
```

Computed per set. Session best = max across all sets of that exercise. Updates PR doc if exceeds previous best.

### 5.3 Total session volume

```
total_volume_kg = Σ (reps × weight_kg) over all sets of all exercises
```

### 5.4 Weekly volume per muscle group

On the progression screen, bucket `reps × weight` into each `muscle_group` tag from the exercise library (an exercise can contribute to multiple buckets; split equally). Sum across the week.

### 5.5 "Last time" hint (inline on log screen)

For each exercise the user adds, query most recent completed set in `workouts` collection for that `exercise_id` (orderBy date desc, limit 1). Show `Last: 60kg × 5`. Green tick if current set beats it in weight OR matches weight with more reps.

---

## 6. Module Structure

Single Gradle module for MVP:

```
app/
  data/
    firestore/              WorkoutRepository, RoutineRepository, BodyweightRepository, PRRepository
    local/                  ExerciseLibraryLoader (reads raw/exercises.json)
    auth/                   AuthRepository
  domain/
    model/                  Workout, Routine, Exercise, Set, PR, BodyweightEntry
    usecase/                CalculateCaloriesUseCase, DetectPRsUseCase, ComputeVolumeUseCase,
                            GetLastSetForExerciseUseCase
  ui/
    auth/                   AuthScreen
    dashboard/              DashboardScreen
    log/                    LogWorkoutScreen, ExercisePickerBottomSheet
    routines/               RoutineListScreen, RoutineEditorScreen
    history/                HistoryScreen, WorkoutDetailScreen
    progression/            ProgressionScreen + chart composables
    bodyweight/             BodyweightLogScreen
    profile/                ProfileScreen
    theme/                  Color.kt, Type.kt, Theme.kt
  di/                       FirebaseModule, RepositoryModule, UseCaseModule
  widget/                   (Phase 4) Glance widget
  resources/raw/            exercises.json
```

---

## 7. Firestore Security Rules (MVP)

```
rules_version = '2';
service cloud.firestore {
  match /databases/{db}/documents {
    match /users/{uid}/{document=**} {
      allow read, write: if request.auth != null && request.auth.uid == uid;
    }
  }
}
```

---

## 8. Build Order

### MVP (v0.1) — ~1.5 weeks evening work

1. Gradle scaffold, Firebase project, `google-services.json`, Hilt, theme
2. Auth (Google Sign-In) + App Check
3. `exercises.json` seed + loader + search
4. Data layer: repos + models + Firestore offline persistence
5. Routine List + Editor screens
6. Log Workout screen (from routine or blank) — the core UX
7. Save-time logic: calorie calc, PR detect, 1RM update, volume cache
8. History + Workout Detail screens
9. Bodyweight Log screen
10. Dashboard (today's routine, latest PRs, this week's volume)
11. Progression screen (three charts)
12. Profile/Settings

### Phase 2 — AI food parser

OpenRouter client, JSON-mode prompt, IFCT food DB cross-reference (for Indian meals accuracy), Log Food screen, Nutrition Dashboard.

### Phase 3 — Health Connect

Request permissions for `Steps` + `SleepSession`, query on resume, display on Dashboard.

### Phase 4 — Glance widget

Quick-log text field → WorkManager job → OpenRouter → Firestore → widget update. Requires Firestore offline persistence (already on).

---

## 9. Stitch Prompt — Features & Options per Screen

> Paste each screen's block into Stitch individually. Features only — no copy, no styling direction, that's for the Stitch designer to fill.

### MVP SCREENS

---

**Screen: Auth**
- Google Sign-In button
- App logo / name
- Terms & Privacy link (small, bottom)

---

**Screen: Dashboard (Home)**
- Greeting bar with user name + current streak count
- "Today's Routine" card: routine name, exercise count, "Start Workout" button
- If no routine assigned today: "No routine for today" + "Start Blank Workout" button
- This Week card: total volume (kg), total workouts, total calories burned
- Recent PRs card: list of last 3 PRs with exercise name, weight, date
- Bodyweight mini-card: latest weight + trend arrow + "Log weight" tap action
- Bottom navigation: Home, History, Progression, Routines, Profile

---

**Screen: Log Workout**
- Top bar: routine name (editable), session date picker, heavy-day toggle
- Duration field (minutes)
- Exercise list (vertically scrollable):
  - Each exercise row: name, muscle groups chip, set count indicator
  - "Last: [weight] × [reps]" hint below exercise name
  - Sets table inside: rows for set number, reps input, weight input, RPE dropdown, delete icon
  - "Add set" button
  - Set row shows green check when it beats last session
- "Add exercise" button (opens exercise picker bottom sheet)
- Reorder exercises via drag handle
- Notes field (multiline)
- Save button (sticky bottom) — disabled until at least 1 exercise with 1 set
- Discard/cancel action in top bar

---

**Screen: Exercise Picker (Bottom Sheet)**
- Search bar (fuzzy match by name + aliases)
- Category filter chips: All, Push, Pull, Legs, Core, Cardio, Mobility
- Equipment filter chips: Barbell, Dumbbell, Cable, Machine, Bodyweight, Other
- Multi-select list of exercises with: name, muscle groups, equipment icon, checkbox
- Selected count badge
- "Add N exercises" button (sticky bottom)
- "Create custom exercise" link at top (opens inline form: name, category, muscle groups, equipment, MET estimate)

---

**Screen: Routine List**
- Grid or list of routine cards, each showing:
  - Routine name
  - Assigned day of week (or "Unassigned")
  - Exercise count
  - Edit + delete icons
- "Create new routine" floating action button
- Empty state with illustration + CTA

---

**Screen: Routine Editor**
- Routine name input
- Day-of-week picker (or "None")
- Exercise list (reorderable):
  - Each row: exercise name, target sets × reps × weight inputs, remove icon
- "Add exercise" button (opens exercise picker)
- Save / Cancel buttons
- Delete routine action (if editing existing)

---

**Screen: Workout History**
- Calendar strip at top (week view, tap date to jump)
- Vertical list grouped by month:
  - Each workout card: date, routine name, duration, total volume, calorie count, PR badge if any
  - Tap → Workout Detail
- Filter button: by routine, by date range
- Empty state

---

**Screen: Workout Detail**
- Header: date, routine name, duration, heavy-day badge if applicable
- Stats row: total volume, calories burned, PR count
- Exercise breakdown:
  - Each exercise: name, sets × reps × weight rows, session-best 1RM, comparison vs previous session (+X kg, ±Y reps)
  - PR badge on any exercise where a PR was set
- Notes section (if any)
- Edit workout button
- Delete workout button (with confirmation)

---

**Screen: Progression / Analytics**
- Exercise selector dropdown at top (defaults to last used)
- Chart 1 — Weight/Rep Scatter: x = date, y = weight, dot size = reps, tap dot → session
- Chart 2 — Estimated 1RM Line: weekly max, trend line overlay
- Chart 3 — Weekly Volume Bar: last 12 weeks, colour-split by muscle group
- Time range toggle: 1M, 3M, 6M, 1Y, All
- PR list below charts: all-time PRs per exercise with date achieved
- Export button (CSV)

---

**Screen: Bodyweight Log**
- Latest weight big display + trend arrow vs last week
- Line chart of bodyweight over time (range toggle)
- "Add entry" button → bottom sheet with weight input + date picker
- List of past entries below chart, swipe to delete

---

**Screen: Profile / Settings**
- User info: name, email, profile photo
- Editable fields: height, weekly volume goal, calorie target, protein target
- Units toggle: metric / imperial
- Section: Connected services — Health Connect status, OpenRouter API key (Phase 2)
- Section: Data — export all data, clear cache
- Section: About — app version, privacy policy, feedback link
- Sign out button

---

### PHASE 2 SCREENS (AI Food Parser)

---

**Screen: Nutrition Dashboard**
- Today's rings: calories, protein, carbs, fats (progress vs target)
- Remaining macros for the day (big numbers)
- Today's meals list: time, description, calorie count, tap to expand
- "Log meal" FAB
- Weekly calorie trend chart (below fold)
- Link to full meal history

---

**Screen: Log Food**
- Big text input: "What did you eat?"
- Microphone button for voice input
- Recent meals shortcuts (chips): "Same as yesterday breakfast" etc.
- On submit → loading state → AI parse result screen
- Parse result:
  - List of detected items, each editable: name, quantity, calories, protein, carbs, fats
  - Total row at bottom
  - "Confirm & save" / "Re-parse with correction" / "Cancel" buttons
- Manual entry toggle (bypasses AI)

---

**Screen: Meal History**
- List grouped by date (Today, Yesterday, earlier with full dates)
- Each row: time, meal description (truncated), total calories + protein
- Tap → Meal Detail
- Swipe to delete
- Search by text
- Filter by date range

---

**Screen: Meal Detail**
- Original text input shown at top
- Item breakdown table (editable)
- Total macros card
- Source badge (AI / Manual / Edited)
- Re-parse button
- Delete button

---

### PHASE 3 — HEALTH CONNECT

No new screens. Dashboard gets two extra cards:
- Steps today (with goal ring)
- Last night's sleep (duration + quality if available)

---

### PHASE 4 — HOME SCREEN WIDGET (Glance)

- Small (2×1): today's calories remaining + progress bar
- Medium (4×2): calories + macros rings + "Tap to log meal" text field
- Large (4×3): above + today's workout status + quick-log button

---

## 10. Google Stitch MCP Setup (for AI coding agents)

Once your Stitch project is ready, expose it to Claude Code / Cursor / Antigravity / any MCP client so the agent building the Android app can pull screens directly.

### 10.1 Enable Stitch API on your Google Cloud project

```bash
# Replace with your actual project ID (the same one used for Firebase)
export PROJECT_ID="your-project-id"

gcloud auth login
gcloud config set project "$PROJECT_ID"
gcloud auth application-default login
gcloud auth application-default set-quota-project "$PROJECT_ID"

# Enable Stitch MCP
gcloud beta services mcp enable stitch.googleapis.com --project="$PROJECT_ID"
```

### 10.2 MCP client config

**Option A — easiest, zero-config proxy (`@_davideast/stitch-mcp`):**

Add to your MCP client config (`claude_desktop_config.json`, Cursor settings, etc.):

```json
{
  "mcpServers": {
    "stitch": {
      "command": "npx",
      "args": ["@_davideast/stitch-mcp", "proxy"]
    }
  }
}
```

First run triggers a setup wizard (gcloud auth + token refresh handled automatically).

**Option B — direct to official Google endpoint (requires manual API key):**

1. In Stitch: Profile → Stitch settings → API key → Create key
2. Config:

```json
{
  "mcpServers": {
    "stitch": {
      "url": "https://stitch.googleapis.com/mcp",
      "type": "http",
      "headers": {
        "Accept": "application/json",
        "X-Goog-Api-Key": "YOUR_STITCH_API_KEY"
      }
    }
  }
}
```

### 10.3 Tools the agent will have access to

- `list_projects` — find your Stitch project
- `list_screens` — see all screens in the project
- `get_screen_code` — fetch HTML/CSS for a given screen
- `get_screen_image` — fetch screenshot (base64)
- `extract_design_dna` — colours, fonts, spacing tokens
- `generate_screen_from_text` — create new screens from prompts

### 10.4 How to use during app build

When you kick off the Android build with Claude Code, include this in the initial prompt:

> "My Stitch project ID is `<PROJECT_ID>`. Use the Stitch MCP server to fetch each screen's HTML and design tokens, then translate them into Jetpack Compose. Reference this plan file for the feature list and data model of each screen."

The agent will then call `list_screens`, pull each one's code + screenshot, and use them as visual reference while writing the Compose code. Design DNA (colours, typography) gets mapped into `theme/Color.kt` and `theme/Type.kt`.

### 10.5 Token refresh

Access tokens expire after ~1 hour. Option A handles refresh automatically. Option B requires regenerating the API key if it expires — check the Stitch settings page.

---

## 11. Open Questions (to resolve before / during build)

- **Streak definition:** Consecutive days with *any* workout, or consecutive days matching the planned routine? (Suggest: latter — it enforces programme adherence.)
- **Routine editing mid-workout:** If you deviate from the routine during a session, does the workout save as "ad hoc" or still reference the routine? (Suggest: still reference, but flag deviation.)
- **Multi-user data sharing:** Since the app is for you + friends, should PRs be visible across users for friendly competition? Post-MVP feature — flag for later.
- **Import path for exercise library:** Curate the ~100-entry JSON manually or scrape a seed from an open dataset (e.g. wger's exercise DB) then annotate with MET values? (Suggest: seed from wger, annotate MET manually — saves hours.)

---

*Built on Android, Kotlin 2.x, Jetpack Compose, Firebase, OpenRouter.*
